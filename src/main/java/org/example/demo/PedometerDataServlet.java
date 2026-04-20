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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

/**
 * Task 2 – Web App Pedometer API
 * Accepts POST /api/steps from the mobile app.
 *
 * Expected JSON payload:
 *   { "steps": 7800, "date": "2026-03-25" }
 *
 * Authentication: Bearer token in the Authorization header.
 *   Authorization: Bearer <token>
 */
@WebServlet("/api/steps")
public class PedometerDataServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // ── 1. Token auth ──────────────────────────────────────────────
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }
        String token = authHeader.substring(7).trim();
        String userEmail;
        try {
            userEmail = DBUtil.validateApiToken(token);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"DB error during token validation\"}");
            return;
        }
        if (userEmail == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"Invalid or expired token\"}");
            return;
        }

        // ── 2. Parse body ───────────────────────────────────────────────
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        String body = sb.toString().trim();

        int steps;
        String dateStr;
        try {
            // simple extraction – no external JSON lib needed
            steps   = Integer.parseInt(body.replaceAll(".*\"steps\"\\s*:\\s*(\\d+).*", "$1"));
            dateStr = body.replaceAll(".*\"date\"\\s*:\\s*\"([^\"]+)\".*", "$1");
            if (dateStr.equals(body)) dateStr = LocalDate.now().toString(); // fallback to today
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Invalid payload – expected {steps:int, date:YYYY-MM-DD}\"}");
            return;
        }

        // ── 3. Persist to user_activity_metrics ────────────────────────
        try (Connection conn = DBUtil.getConnection()) {
            // Upsert: overwrite today's record if already present
            String upsert =
                "INSERT INTO user_activity_metrics (user_id, step_count, activity_date, sync_source) " +
                "VALUES (?, ?, ?, 'mobile') " +
                "ON CONFLICT (user_id, activity_date) " +
                "DO UPDATE SET step_count = EXCLUDED.step_count, sync_source = 'mobile'";

            try (PreparedStatement ps = conn.prepareStatement(upsert)) {
                ps.setString(1, userEmail);
                ps.setInt(2, steps);
                ps.setDate(3, Date.valueOf(dateStr));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            // H2 doesn't support ON CONFLICT – fall back to simple insert
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO user_activity_metrics (user_id, step_count, activity_date, sync_source) " +
                     "VALUES (?, ?, ?, 'mobile')")) {
                ps.setString(1, userEmail);
                ps.setInt(2, steps);
                ps.setDate(3, Date.valueOf(dateStr));
                ps.executeUpdate();
            } catch (Exception ex) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"error\":\"" + ex.getMessage() + "\"}");
                return;
            }
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        out.write("{\"status\":\"ok\",\"user\":\"" + userEmail + "\",\"steps\":" + steps + "}");
    }
}
