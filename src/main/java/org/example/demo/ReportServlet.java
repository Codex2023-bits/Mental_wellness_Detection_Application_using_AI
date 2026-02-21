package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

@WebServlet("/report-api")
public class ReportServlet extends HttpServlet {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL = "tinyllama";

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
            String prompt = "You are a friendly wellness coach. Based on the following 7-day wellness data, " +
                    "write a short, encouraging wellness report (3-4 paragraphs). Include observations about " +
                    "patterns, suggestions for improvement, and positive reinforcement. Keep it concise and warm.\n\n" +
                    "MEDITATION LOG (last 7 days):\n" + meditationData +
                    "\nEXERCISE LOG (last 7 days):\n" + exerciseData +
                    "\nWrite the report now:";

            // Call Ollama
            String aiResponse = callOllama(prompt);
            out.write("{\"report\":\"" + escapeJson(aiResponse) + "\"}");

        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Connection refused")) {
                out.write("{\"error\":\"Ollama is not running. Start it with: ollama serve\"}");
            } else {
                out.write("{\"error\":\"" + escapeJson(msg) + "\"}");
            }
        }
    }

    private String callOllama(String prompt) throws IOException {
        URL url = new URL(OLLAMA_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(120000); // 2 min for generation

        // Build JSON request (stream: false for single response)
        String jsonBody = "{\"model\":\"" + MODEL + "\",\"prompt\":\"" +
                escapeJson(prompt) + "\",\"stream\":false}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes("UTF-8"));
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        if (status >= 200 && status < 300) {
            // Extract "response" field from JSON manually
            String json = sb.toString();
            int idx = json.indexOf("\"response\":\"");
            if (idx != -1) {
                int start = idx + 12;
                int end = findJsonStringEnd(json, start);
                return unescapeJson(json.substring(start, end));
            }
            return "Report generated but could not parse response.";
        } else {
            throw new IOException("Ollama returned status " + status + ": " + sb);
        }
    }

    /** Find the end of a JSON string value (handling escaped quotes) */
    private int findJsonStringEnd(String json, int start) {
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == '\\') {
                i++; // skip escaped char
            } else if (json.charAt(i) == '"') {
                return i;
            }
        }
        return json.length();
    }

    private String unescapeJson(String text) {
        return text.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
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
