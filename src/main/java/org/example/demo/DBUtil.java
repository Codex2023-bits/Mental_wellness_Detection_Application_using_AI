package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    public static Connection getConnection() throws Exception {
        Class.forName("org.h2.Driver");

        return DriverManager.getConnection(
                "jdbc:h2:~/testdb",
                "sa",
                "");
    }
}
