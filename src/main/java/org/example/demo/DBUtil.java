package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    /*
     * POOLER LOGIC:
     * The host should end in '.pooler.supabase.com' and the port is usually 5432
     * for Session mode.
     * Username format: postgres.[PROJECT_REF]
     */
    private static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL")
            : "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres";

    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER")
            : "postgres.pbwfzhhuchelilcmtljm";

    private static final String PASS = System.getenv("DB_PASS");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}