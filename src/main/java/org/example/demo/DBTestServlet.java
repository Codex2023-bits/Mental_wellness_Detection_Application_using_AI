package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;

@WebServlet("/db-setup")
public class DBTestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Create the users table
            String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                                 "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                 "email VARCHAR(255) NOT NULL UNIQUE, " +
                                 "password VARCHAR(255) NOT NULL)";
            stmt.execute(createTable);

            // 2. Insert a demo user (using MERGE to avoid duplicate errors if run twice)
            String insertUser = "MERGE INTO users (email, password) KEY(email) " +
                                "VALUES ('babishake8@gmail.com', 'password')";
            stmt.execute(insertUser);

            out.println("<h3>Database Setup Successful!</h3>");
            out.println("<p>Table 'users' created and demo user inserted.</p>");
            out.println("<a href='index.jsp'>Go to Login Page</a>");

        } catch (Exception e) {
            out.println("<h3>Error during database setup:</h3>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace();
        }
    }
}