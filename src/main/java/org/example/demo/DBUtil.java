package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {

    /*
     * POOLER LOGIC:
     * The host should end in '.pooler.supabase.com' and the port is usually 5432
     * for Session mode.
     * Username format: postgres.[PROJECT_REF]
     */

    private static String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:h2:mem:wellness;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";
    static {
        // Ensure SSL for Supabase PostgreSQL connections
        if (URL != null && URL.startsWith("jdbc:postgresql") && !URL.contains("sslmode")) {
            URL = URL + "?sslmode=require";
        }
    }

    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER")
            : "sa";

    private static final String PASS = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS")
            : "";

    static {
        try {
            if (URL.contains("postgresql")) {
                Class.forName("org.postgresql.Driver");
            } else {
                Class.forName("org.h2.Driver");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Database Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Returns the last 7 days of step activity for a given user.
     * Each entry is a map with keys: "date" (String) and "steps" (Integer).
     */
    public static List<Map<String, Object>> getLast7DaysActivity(String userEmail) throws SQLException {
        String sql = "SELECT activity_date, SUM(step_count) AS total_steps " +
                     "FROM user_activity_metrics " +
                     "WHERE user_id = ? AND activity_date >= CURRENT_DATE - INTERVAL '7 days' " +
                     "GROUP BY activity_date ORDER BY activity_date ASC";
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("date", rs.getDate("activity_date").toString());
                    row.put("steps", rs.getInt("total_steps"));
                    results.add(row);
                }
            }
        }
        return results;
    }

    /**
     * Validates an API token and returns the user email if valid, null otherwise.
     */
    public static String validateApiToken(String token) throws SQLException {
        String sql = "SELECT user_email FROM api_tokens WHERE token = ? AND expires_at > CURRENT_TIMESTAMP";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_email");
                }
            }
        }
        return null;
    }
}