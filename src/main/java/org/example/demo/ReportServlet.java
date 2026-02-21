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

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_MODEL = "llama-3.1-8b-instant";

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

        String apiKey = System.getenv("GROQ_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter()
                    .write("{\"error\":\"AI service is not configured. Please contact the administrator.\"}");
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
            String userMessage = "You are a friendly wellness coach. Based on the following 7-day wellness data, " +
                    "write a short, encouraging wellness report (3-4 paragraphs). Include observations about " +
                    "patterns, suggestions for improvement, and positive reinforcement. Keep it concise and warm.\n\n" +
                    "MEDITATION LOG (last 7 days):\n" + meditationData +
                    "\nEXERCISE LOG (last 7 days):\n" + exerciseData +
                    "\nWrite the report now:";

            // Call Groq
            String aiResponse = callGroq(apiKey, userMessage);
            out.write("{\"report\":\"" + escapeJson(aiResponse) + "\"}");

        } catch (Exception e) {
            out.write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private String callGroq(String apiKey, String userMessage) throws IOException {
        URL url = new URL(GROQ_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(60000);

        // Groq uses OpenAI-compatible Chat Completions format
        String jsonBody = "{" +
                "\"model\":\"" + GROQ_MODEL + "\"," +
                "\"messages\":[{\"role\":\"user\",\"content\":\"" + escapeJson(userMessage) + "\"}]," +
                "\"max_tokens\":1024," +
                "\"temperature\":0.7" +
                "}";

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
            // Parse: choices[0].message.content
            String json = sb.toString();
            String marker = "\"content\":\"";
            int idx = json.indexOf(marker);
            if (idx != -1) {
                int start = idx + marker.length();
                int end = findJsonStringEnd(json, start);
                return unescapeJson(json.substring(start, end));
            }
            return "Report generated but could not parse response.";
        } else {
            throw new IOException("Groq API returned status " + status + ": " + sb);
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
