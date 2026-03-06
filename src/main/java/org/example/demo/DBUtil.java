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
            : "jdbc:h2:mem:wellness;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";

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
}