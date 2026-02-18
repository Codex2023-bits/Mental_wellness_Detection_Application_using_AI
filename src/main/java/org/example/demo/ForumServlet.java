package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/forum-api")
public class ForumServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Session check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Not logged in\"}");
            return;
        }

        String currentUser = (String) session.getAttribute("user");
        String action = request.getParameter("action");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection()) {

            if ("users".equals(action)) {
                // Get all users except the current one
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT email FROM users WHERE email != ? ORDER BY email");
                ps.setString(1, currentUser);
                ResultSet rs = ps.executeQuery();

                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                while (rs.next()) {
                    if (!first)
                        json.append(",");
                    json.append("{\"email\":\"")
                            .append(escapeJson(rs.getString("email")))
                            .append("\"}");
                    first = false;
                }
                json.append("]");
                out.write(json.toString());

            } else if ("messages".equals(action)) {
                // Get conversation between current user and specified user
                String withUser = request.getParameter("with");
                if (withUser == null || withUser.isEmpty()) {
                    out.write("{\"error\":\"Missing 'with' parameter\"}");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "SELECT sender_email, receiver_email, content, sent_at FROM messages " +
                                "WHERE (sender_email = ? AND receiver_email = ?) " +
                                "   OR (sender_email = ? AND receiver_email = ?) " +
                                "ORDER BY sent_at ASC");
                ps.setString(1, currentUser);
                ps.setString(2, withUser);
                ps.setString(3, withUser);
                ps.setString(4, currentUser);
                ResultSet rs = ps.executeQuery();

                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                while (rs.next()) {
                    if (!first)
                        json.append(",");
                    json.append("{")
                            .append("\"sender\":\"").append(escapeJson(rs.getString("sender_email"))).append("\",")
                            .append("\"receiver\":\"").append(escapeJson(rs.getString("receiver_email"))).append("\",")
                            .append("\"content\":\"").append(escapeJson(rs.getString("content"))).append("\",")
                            .append("\"sentAt\":\"").append(rs.getTimestamp("sent_at").toString()).append("\"")
                            .append("}");
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

        // Session check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Not logged in\"}");
            return;
        }

        String currentUser = (String) session.getAttribute("user");
        String action = request.getParameter("action");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if ("send".equals(action)) {
            String receiver = request.getParameter("receiver");
            String content = request.getParameter("content");

            if (receiver == null || content == null || receiver.isEmpty() || content.isEmpty()) {
                out.write("{\"error\":\"Missing receiver or content\"}");
                return;
            }

            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO messages (sender_email, receiver_email, content) VALUES (?, ?, ?)");
                ps.setString(1, currentUser);
                ps.setString(2, receiver);
                ps.setString(3, content);
                ps.executeUpdate();

                out.write("{\"success\":true}");

            } catch (Exception e) {
                response.setStatus(500);
                out.write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        } else {
            out.write("{\"error\":\"Unknown action\"}");
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
