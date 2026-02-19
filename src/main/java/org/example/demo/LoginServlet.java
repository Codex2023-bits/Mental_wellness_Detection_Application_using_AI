package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null) {
            response.sendRedirect("index.jsp?error=invalid");
            return;
        }

        try (java.sql.Connection conn = DBUtil.getConnection()) {
            java.sql.PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE email = ? AND password = ?");
            ps.setString(1, email);
            ps.setString(2, password);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Login successful
                HttpSession session = request.getSession();
                session.setAttribute("user", email);

                // Store login time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                session.setAttribute("loginTime", LocalDateTime.now().format(dtf));

                response.sendRedirect("dashboard.jsp");
            } else {
                // Invalid credentials
                response.sendRedirect("index.jsp?error=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=server");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
