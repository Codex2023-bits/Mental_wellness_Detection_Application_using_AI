package org.example.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/dbtest")
public class DBTestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Connection conn = DBUtil.getConnection();
            out.println("<h2>Database Connected Successfully!</h2>");
            conn.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
