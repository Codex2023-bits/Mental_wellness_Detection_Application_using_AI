package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (email == null || password == null || confirmPassword == null || email.isEmpty() || password.isEmpty()) {
            response.sendRedirect("signup.jsp?error=invalid");
            return;
        }

        if (!password.equals(confirmPassword)) {
            response.sendRedirect("signup.jsp?error=mismatch");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            // Check if user already exists
            String checkSql = "SELECT email FROM users WHERE email = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setString(1, email);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        response.sendRedirect("signup.jsp?error=exists");
                        return;
                    }
                }
            }

            // Insert new user
            String insertSql = "INSERT INTO users (email, password) VALUES (?, ?)";
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setString(1, email);
                psInsert.setString(2, password);
                int affectedRows = psInsert.executeUpdate();

                if (affectedRows > 0) {
                    // Registration successful, redirect to login with success message
                    response.sendRedirect("index.jsp?success=registered");
                } else {
                    response.sendRedirect("signup.jsp?error=failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("signup.jsp?error=server");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("signup.jsp");
    }
}
