package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/dashboard-api")
public class DashboardServlet extends HttpServlet {

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
        String action = request.getParameter("action");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection()) {

            if ("meditation-stats".equals(action)) {
                // Get last 7 days of meditation data, grouped by date
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT logged_date, SUM(duration_minutes) AS total_minutes " +
                                "FROM meditation_logs WHERE email = ? " +
                                "AND logged_date >= DATEADD('DAY', -6, CURRENT_DATE) " +
                                "GROUP BY logged_date ORDER BY logged_date");
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                while (rs.next()) {
                    if (!first)
                        json.append(",");
                    json.append("{\"date\":\"").append(rs.getDate("logged_date").toString())
                            .append("\",\"minutes\":").append(rs.getInt("total_minutes")).append("}");
                    first = false;
                }
                json.append("]");
                out.write(json.toString());

            } else if ("exercise-stats".equals(action)) {
                // Get last 7 days of exercise data, grouped by date
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT logged_date, SUM(duration_minutes) AS total_minutes " +
                                "FROM exercise_logs WHERE email = ? " +
                                "AND logged_date >= DATEADD('DAY', -6, CURRENT_DATE) " +
                                "GROUP BY logged_date ORDER BY logged_date");
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                while (rs.next()) {
                    if (!first)
                        json.append(",");
                    json.append("{\"date\":\"").append(rs.getDate("logged_date").toString())
                            .append("\",\"minutes\":").append(rs.getInt("total_minutes")).append("}");
                    first = false;
                }
                json.append("]");
                out.write(json.toString());

            } else {
                out.write("{\"error\":\"Unknown action\"}");
            }

        } catch (Exception e) {
            response.setStatus(500);
            out.write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Not logged in\"}");
            return;
        }

        String email = (String) session.getAttribute("user");
        String action = request.getParameter("action");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection()) {

            if ("log-meditation".equals(action)) {
                String minutes = request.getParameter("minutes");
                String date = request.getParameter("date");
                if (minutes == null || date == null) {
                    out.write("{\"error\":\"Missing minutes or date\"}");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO meditation_logs (email, duration_minutes, logged_date) VALUES (?, ?, ?)");
                ps.setString(1, email);
                ps.setInt(2, Integer.parseInt(minutes));
                ps.setDate(3, Date.valueOf(date));
                ps.executeUpdate();
                out.write("{\"success\":true}");

            } else if ("log-exercise".equals(action)) {
                String type = request.getParameter("type");
                String minutes = request.getParameter("minutes");
                String date = request.getParameter("date");
                if (type == null || minutes == null || date == null) {
                    out.write("{\"error\":\"Missing type, minutes, or date\"}");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO exercise_logs (email, exercise_type, duration_minutes, logged_date) VALUES (?, ?, ?, ?)");
                ps.setString(1, email);
                ps.setString(2, type);
                ps.setInt(3, Integer.parseInt(minutes));
                ps.setDate(4, Date.valueOf(date));
                ps.executeUpdate();
                out.write("{\"success\":true}");

            } else {
                out.write("{\"error\":\"Unknown action\"}");
            }

        } catch (Exception e) {
            response.setStatus(500);
            out.write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private String escapeJson(String text) {
        if (text == null)
            return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
