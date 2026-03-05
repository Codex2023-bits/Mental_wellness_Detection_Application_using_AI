package org.example.demo;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

@WebListener
public class DBInitializer implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent sce) {
                System.out.println("Initializing Database...");
                try (Connection conn = DBUtil.getConnection();
                                Statement stmt = conn.createStatement()) {

                        // 1. Create the users table
                        String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                                        "id SERIAL PRIMARY KEY, " +
                                        "email VARCHAR(255) NOT NULL UNIQUE, " +
                                        "password VARCHAR(255) NOT NULL)";
                        stmt.execute(createTable);

                        // 2. Insert demo users (with hashed passwords)
                        String hashedPwd = BCrypt.hashpw("password", BCrypt.gensalt());
                        String[] demoUsers = {
                                        "INSERT INTO users (email, password) VALUES ('babishake8@gmail.com', '"
                                                        + hashedPwd
                                                        + "') ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password",
                                        "INSERT INTO users (email, password) VALUES ('alice@example.com', '" + hashedPwd
                                                        + "') ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password",
                                        "INSERT INTO users (email, password) VALUES ('bob@example.com', '" + hashedPwd
                                                        + "') ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password",
                                        "INSERT INTO users (email, password) VALUES ('charlie@example.com', '"
                                                        + hashedPwd
                                                        + "') ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password"
                        };
                        for (String sql : demoUsers) {
                                stmt.execute(sql);
                        }

                        // 3. Create the messages table
                        String createMessages = "CREATE TABLE IF NOT EXISTS messages (" +
                                        "id SERIAL PRIMARY KEY, " +
                                        "sender_email VARCHAR(255) NOT NULL, " +
                                        "receiver_email VARCHAR(255) NOT NULL, " +
                                        "content TEXT NOT NULL, " +
                                        "sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                        stmt.execute(createMessages);

                        // 4. Create meditation logs table
                        String createMeditation = "CREATE TABLE IF NOT EXISTS meditation_logs (" +
                                        "id SERIAL PRIMARY KEY, " +
                                        "email VARCHAR(255) NOT NULL, " +
                                        "duration_minutes INT NOT NULL, " +
                                        "logged_date DATE NOT NULL)";
                        stmt.execute(createMeditation);

                        // 5. Create exercise logs table
                        String createExercise = "CREATE TABLE IF NOT EXISTS exercise_logs (" +
                                        "id SERIAL PRIMARY KEY, " +
                                        "email VARCHAR(255) NOT NULL, " +
                                        "exercise_type VARCHAR(100) NOT NULL, " +
                                        "duration_minutes INT NOT NULL, " +
                                        "logged_date DATE NOT NULL)";
                        stmt.execute(createExercise);

                        System.out.println("Database Initialization Successful!");

                } catch (Exception e) {
                        System.err.println("Error during database initialization:");
                        e.printStackTrace();
                }
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
                // Cleanup resources if needed
        }
}
