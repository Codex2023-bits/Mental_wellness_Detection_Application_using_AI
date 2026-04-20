package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mindrot.jbcrypt.BCrypt;

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

        try (java.sql.Connection conn = DBUtil.getConnection();
                java.sql.PreparedStatement ps = conn.prepareStatement(
                        "SELECT password FROM users WHERE email = ?");) {
            ps.setString(1, email);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");

                if (BCrypt.checkpw(password, storedHash)) {
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
            } else {
                // User not found
                response.sendRedirect("index.jsp?error=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                java.nio.file.Files.writeString(java.nio.file.Paths.get("backend_error.txt"), e.toString() + "\n" + java.util.Arrays.toString(e.getStackTrace()));
            } catch(Exception ignored){}
            response.sendRedirect("index.jsp?error=server&msg=" + java.net.URLEncoder.encode(e.getMessage() != null ? e.getMessage() : "Unknown", "UTF-8"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
