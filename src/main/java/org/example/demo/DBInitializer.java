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

                        // 2. Refresh demo users (with hashed passwords)
                        String hashedPwd = BCrypt.hashpw("password", BCrypt.gensalt());
                        

                        // Delete existing to avoid conflicts during initialization
                        stmt.execute("DELETE FROM users WHERE email IN ('babishake8@gmail.com', 'alice@example.com', 'bob@example.com', 'charlie@example.com')");

                        String[] demoUsers = {
                                        "INSERT INTO users (email, password) VALUES ('babishake8@gmail.com', '"
                                                        + hashedPwd + "')",
                                        "INSERT INTO users (email, password) VALUES ('alice@example.com', '" + hashedPwd
                                                        + "')",
                                        "INSERT INTO users (email, password) VALUES ('bob@example.com', '" + hashedPwd
                                                        + "')",
                                        "INSERT INTO users (email, password) VALUES ('charlie@example.com', '"
                                                        + hashedPwd + "')"
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

                        // 6. Create pedometer logs table
                        String createPedometer = "CREATE TABLE IF NOT EXISTS pedometer_logs (" +
                                        "id SERIAL PRIMARY KEY, " +
                                        "email VARCHAR(255) NOT NULL, " +
                                        "steps INT NOT NULL, " +
                                        "distance_km DOUBLE PRECISION NOT NULL, " +
                                        "calories_burned INT NOT NULL, " +
                                        "logged_date DATE NOT NULL)";
                        stmt.execute(createPedometer);

                        // 7. Create user_activity_metrics table (from mobile pedometer pushes)
                        String createActivityMetrics = "CREATE TABLE IF NOT EXISTS user_activity_metrics (" +
                                        "id SERIAL PRIMARY KEY, " +
                                        "user_id VARCHAR(255) NOT NULL, " +
                                        "step_count INT NOT NULL, " +
                                        "activity_date DATE NOT NULL, " +
                                        "sync_source VARCHAR(50) DEFAULT 'mobile', " +
                                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                        "UNIQUE(user_id, activity_date))";
                        stmt.execute(createActivityMetrics);

                        // 8. Create api_tokens table for mobile authentication
                        String createApiTokens = "CREATE TABLE IF NOT EXISTS api_tokens (" +
                                        "id SERIAL PRIMARY KEY, " +
                                        "user_email VARCHAR(255) NOT NULL, " +
                                        "token VARCHAR(64) NOT NULL UNIQUE, " +
                                        "expires_at TIMESTAMP NOT NULL, " +
                                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                        stmt.execute(createApiTokens);

                        // 9. Create user_steps table (simple alias used by PedometerServlet)
                        String createUserSteps =
                                "CREATE TABLE IF NOT EXISTS user_steps (" +
                                "id SERIAL PRIMARY KEY, " +
                                "email VARCHAR(255) NOT NULL, " +
                                "step_count INT NOT NULL, " +
                                "log_date DATE NOT NULL, " +
                                "UNIQUE(email, log_date))";
                        stmt.execute(createUserSteps);
                        System.out.println("Database Initialization Successful!");

                } catch (Exception e) {
                        System.err.println("Error during database initialization:");
                        e.printStackTrace();
                        try {
                            java.nio.file.Files.writeString(java.nio.file.Paths.get("db_error.txt"), e.toString() + "\n" + java.util.Arrays.toString(e.getStackTrace()));
                        } catch(Exception ignored){}
                }
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
                // Cleanup resources if needed
        }
}
