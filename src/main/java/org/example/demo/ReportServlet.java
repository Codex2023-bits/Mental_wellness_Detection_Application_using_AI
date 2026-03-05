package org.example.demo;

import dev.langchain4j.model.ollama.OllamaChatModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.time.Duration;

@WebServlet("/report-api")
public class ReportServlet extends HttpServlet {

    private OllamaChatModel model;

    @Override
    public void init() throws ServletException {
        // Initialize the local Ollama model
        model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Not logged in\"}");
            return;
        }

        String email = (String) session.getAttribute("user");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection()) {

            // Fetch meditation data (last 7 days)
            StringBuilder meditationData = new StringBuilder();
            PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT logged_date, SUM(duration_minutes) AS total " +
                            "FROM meditation_logs WHERE email = ? " +
                            "AND logged_date >= CURRENT_DATE - INTERVAL '6 days' " +
                            "GROUP BY logged_date ORDER BY logged_date");
            ps1.setString(1, email);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                meditationData.append(rs1.getDate("logged_date"))
                        .append(": ").append(rs1.getInt("total")).append(" minutes\n");
            }
            if (meditationData.length() == 0)
                meditationData.append("No meditation data recorded.\n");

            // Fetch exercise data (last 7 days)
            StringBuilder exerciseData = new StringBuilder();
            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT logged_date, exercise_type, SUM(duration_minutes) AS total " +
                            "FROM exercise_logs WHERE email = ? " +
                            "AND logged_date >= CURRENT_DATE - INTERVAL '6 days' " +
                            "GROUP BY logged_date, exercise_type ORDER BY logged_date");
            ps2.setString(1, email);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                exerciseData.append(rs2.getDate("logged_date"))
                        .append(": ").append(rs2.getString("exercise_type"))
                        .append(" - ").append(rs2.getInt("total")).append(" minutes\n");
            }
            if (exerciseData.length() == 0)
                exerciseData.append("No exercise data recorded.\n");

            // Build prompt
            String systemPrompt = "You are a friendly wellness coach. Based on the following 7-day wellness data, " +
                    "write a short, encouraging wellness report (3-4 paragraphs). Include observations about " +
                    "patterns, suggestions for improvement, and positive reinforcement. Keep it concise and warm.";

            String userData = "USER: " + email + "\n\n" +
                    "MEDITATION LOG (last 7 days):\n" + meditationData +
                    "\nEXERCISE LOG (last 7 days):\n" + exerciseData;

            // Call Local AI (Ollama)
            String aiResponse = model.generate(systemPrompt + "\n\n" + userData);

            out.write("{\"report\":\"" + escapeJson(aiResponse) + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"error\":\"Failed to connect to local AI. Ensure Ollama is running with llama3 model.\"}");
        }
    }

    private String escapeJson(String text) {
        if (text == null)
            return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
