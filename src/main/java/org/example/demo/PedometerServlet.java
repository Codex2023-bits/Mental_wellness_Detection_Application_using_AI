package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * PedometerServlet — Receives and persists step data from the Android mobile application.
 * * Endpoint: POST /pedometer
 * * Logic:
 * 1. Validates the Bearer token via DBUtil.
 * 2. Parses JSON: { "email": "...", "steps": 5000, "date": "yyyy-MM-dd" }
 * 3. Updates 'user_steps' for the simple log.
 * 4. Updates 'user_activity_metrics' for the AI analysis pipeline.
 */
@WebServlet("/pedometer")
public class PedometerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // 1. Bearer-token authentication
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }
        String rawToken = authHeader.substring(7).trim();

        String tokenOwner;
        try {
            // Validates token against the api_tokens table created in DBInitializer
            tokenOwner = DBUtil.validateApiToken(rawToken); 
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Database error during token validation\"}");
            return;
        }

        if (tokenOwner == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"Invalid or expired token\"}");
            return;
        }

        // 2. Read and parse JSON body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String body = sb.toString().trim();

        String email = extractJsonString(body, "email");
        String dateStr = extractJsonString(body, "date");
        int steps;
        try {
            steps = Integer.parseInt(extractJsonValue(body, "steps"));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"'steps' must be a valid integer\"}");
            return;
        }

        // Validate mandatory fields
        if (email == null || email.isEmpty() || dateStr == null || dateStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Required fields 'email' or 'date' are missing\"}");
            return;
        }

        // Security check: Ensure the email in payload matches the authenticated token owner
        if (!email.equalsIgnoreCase(tokenOwner)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"Authenticated user does not match payload email\"}");
            return;
        }

        // 3. Persist data using UPSERT (Update if exists, else Insert)
        try (Connection conn = DBUtil.getConnection()) {

            // 3a. Update 'user_steps' table
            String upsertUserSteps =
                "INSERT INTO user_steps (email, step_count, log_date) " +
                "VALUES (?, ?, CAST(? AS DATE)) " +
                "ON CONFLICT (email, log_date) DO UPDATE SET step_count = EXCLUDED.step_count";
            
            try (PreparedStatement ps1 = conn.prepareStatement(upsertUserSteps)) {
                ps1.setString(1, email);
                ps1.setInt(2, steps);
                ps1.setString(3, dateStr);
                ps1.executeUpdate();
            }

            // 3b. Update 'user_activity_metrics' table (used by ReportServlet/LangChain4j)
            String upsertMetrics =
                "INSERT INTO user_activity_metrics (user_id, step_count, activity_date, sync_source) " +
                "VALUES (?, ?, CAST(? AS DATE), 'mobile') " +
                "ON CONFLICT (user_id, activity_date) DO UPDATE SET step_count = EXCLUDED.step_count";
            
            try (PreparedStatement ps2 = conn.prepareStatement(upsertMetrics)) {
                ps2.setString(1, email);
                ps2.setInt(2, steps);
                ps2.setString(3, dateStr);
                ps2.executeUpdate();
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"status\":\"synced\", \"steps\":" + steps + ", \"date\":\"" + dateStr + "\"}");

        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Database persistence error: " + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    // Helper methods for simple JSON parsing without external dependencies
    private String extractJsonString(String json, String key) {
        String search = "\"" + key + "\"";
        int ki = json.indexOf(search);
        if (ki < 0) return null;
        int colon = json.indexOf(':', ki + search.length());
        if (colon < 0) return null;
        int open = json.indexOf('"', colon + 1);
        if (open < 0) return null;
        int close = json.indexOf('"', open + 1);
        if (close < 0) return null;
        return json.substring(open + 1, close);
    }

    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\"";
        int ki = json.indexOf(search);
        if (ki < 0) return null;
        int colon = json.indexOf(':', ki + search.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        int end = start;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') end++;
        return json.substring(start, end).trim();
    }
}