package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Simple validation (replace with database validation in production)
        if (email != null && password != null &&
                email.equals("babishake8@gmail.com") && password.equals("password")) {

            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", email);
            session.setAttribute("email", email);

            // Redirect to dashboard
            response.sendRedirect("dashboard.jsp");
        } else {
            // Login failed - redirect with error parameter
            response.sendRedirect("index.jsp?error=invalid");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
