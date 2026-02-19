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

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
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

                        // 2. Insert demo users (using MERGE to avoid duplicate errors if run twice)
                        String insertUser1 = "MERGE INTO users (email, password) KEY(email) " +
                                        "VALUES ('babishake8@gmail.com', 'password')";
                        stmt.execute(insertUser1);

                        String insertUser2 = "MERGE INTO users (email, password) KEY(email) " +
                                        "VALUES ('alice@example.com', 'password')";
                        stmt.execute(insertUser2);

                        String insertUser3 = "MERGE INTO users (email, password) KEY(email) " +
                                        "VALUES ('bob@example.com', 'password')";
                        stmt.execute(insertUser3);

                        String insertUser4 = "MERGE INTO users (email, password) KEY(email) " +
                                        "VALUES ('charlie@example.com', 'password')";
                        stmt.execute(insertUser4);

                        // 3. Create the messages table for private chat
                        String createMessages = "CREATE TABLE IF NOT EXISTS messages (" +
                                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "sender_email VARCHAR(255) NOT NULL, " +
                                        "receiver_email VARCHAR(255) NOT NULL, " +
                                        "content TEXT NOT NULL, " +
                                        "sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                        stmt.execute(createMessages);

                        // 4. Create meditation logs table
                        String createMeditation = "CREATE TABLE IF NOT EXISTS meditation_logs (" +
                                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "email VARCHAR(255) NOT NULL, " +
                                        "duration_minutes INT NOT NULL, " +
                                        "logged_date DATE NOT NULL)";
                        stmt.execute(createMeditation);

                        // 5. Create exercise logs table
                        String createExercise = "CREATE TABLE IF NOT EXISTS exercise_logs (" +
                                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "email VARCHAR(255) NOT NULL, " +
                                        "exercise_type VARCHAR(100) NOT NULL, " +
                                        "duration_minutes INT NOT NULL, " +
                                        "logged_date DATE NOT NULL)";
                        stmt.execute(createExercise);

                        out.println("<h3>Database Setup Successful!</h3>");
                        out.println("<p>All tables created. Demo users inserted.</p>");
                        out.println("<a href='index.jsp'>Go to Login Page</a>");

                } catch (Exception e) {
                        out.println("<h3>Error during database setup:</h3>");
                        out.println("<pre>" + e.getMessage() + "</pre>");
                        e.printStackTrace();
                }
        }
}