package org.example.demo;

import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.time.Duration;

@WebServlet("/report-api")
public class ReportServlet extends HttpServlet {

    private OpenAiChatModel model;

    @Override
    public void init() throws ServletException {
        // Retrieve Groq API Key from environment or use fallback
        String apiKey = System.getenv("GROQ_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = "YOUR_GROQ_API_KEY"; // Ensure this matches Environment Variable, do not hardcode real keys!
            System.err.println("WARNING: GROQ_API_KEY environment variable is not set!");
        }

        model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://api.groq.com/openai/v1")
                .modelName("llama-3.1-8b-instant")
                .timeout(Duration.ofSeconds(120))
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

            // Fetch pedometer data (last 7 days) – manual logs
            StringBuilder pedometerData = new StringBuilder();
            PreparedStatement ps3 = conn.prepareStatement(
                    "SELECT logged_date, SUM(steps) AS total_steps, SUM(distance_km) AS total_dist, SUM(calories_burned) AS total_cal " +
                            "FROM pedometer_logs WHERE email = ? " +
                            "AND logged_date >= CURRENT_DATE - INTERVAL '6 days' " +
                            "GROUP BY logged_date ORDER BY logged_date");
            ps3.setString(1, email);
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) {
                pedometerData.append(rs3.getDate("logged_date"))
                        .append(": ").append(rs3.getInt("total_steps")).append(" steps")
                        .append(", ").append(rs3.getDouble("total_dist")).append(" km")
                        .append(", ").append(rs3.getInt("total_cal")).append(" kcal\n");
            }
            if (pedometerData.length() == 0)
                pedometerData.append("No pedometer data recorded.\n");

            // Task 3 – Fetch Behavioral Activation data (mobile health connect)
            StringBuilder mobileStepData = new StringBuilder();
            try {
                for (java.util.Map<String, Object> row : DBUtil.getLast7DaysActivity(email)) {
                    mobileStepData.append(row.get("date"))
                            .append(": ").append(row.get("steps")).append(" steps (mobile)\n");
                }
            } catch (Exception ignored) {}
            if (mobileStepData.length() == 0)
                mobileStepData.append("No mobile step data available.\n");

            // Compute 7-day average steps from mobile data for correlation
            int totalMobileSteps = 0;
            int mobileDataDays  = 0;
            try {
                for (java.util.Map<String, Object> row : DBUtil.getLast7DaysActivity(email)) {
                    totalMobileSteps += (int) row.get("steps");
                    mobileDataDays++;
                }
            } catch (Exception ignored) {}
            double avgSteps = mobileDataDays > 0 ? (double) totalMobileSteps / mobileDataDays : 0;
            String activityLevel = avgSteps >= 8000 ? "HIGH" : avgSteps >= 4000 ? "MODERATE" : "LOW";

            // Build prompt
            String systemPrompt = "You are a wellness coach specialising in Behavioural Activation. " +
                    "Based on the 7-day data below, write a short, encouraging report. " +
                    "Pay special attention to the MOBILE STEP DATA section: if activity_level is LOW or MODERATE, " +
                    "explicitly note how low physical activity may be correlating with a decline in mental wellness " +
                    "and suggest one concrete Behavioural Activation exercise. " +
                    "CRITICAL: At the very end of your report, include a score and status on a new line in this exact format: " +
                    "[SCORE: X/10] [STATUS: Your Status] where X is 1-10 and 'Your Status' is a short 1-2 word status.";

            String userData = "USER: " + email + "\n\n" +
                    "MEDITATION LOG (last 7 days):\n" + meditationData +
                    "\nEXERCISE LOG (last 7 days):\n" + exerciseData +
                    "\nPEDOMETER LOG (last 7 days):\n" + pedometerData +
                    "\nMOBILE STEP DATA – Behavioural Activation (Health Connect):\n" + mobileStepData +
                    "\nCALCULATED ACTIVITY LEVEL: " + activityLevel +
                    " (7-day avg: " + String.format("%.0f", avgSteps) + " steps/day)";

            // Call Groq AI via OpenAiChatModel
            String aiResponse = model.generate(systemPrompt + "\n\n" + userData);

            out.write("{\"report\":\"" + escapeJson(aiResponse) + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"error\":\"Failed to connect to AI. Ensure GROQ_API_KEY is valid.\"}");
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
