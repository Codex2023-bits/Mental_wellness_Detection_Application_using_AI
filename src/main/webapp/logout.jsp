<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% // Step 13: Invalidate the session using session.invalidate() when the user logs out if (session !=null) {
        session.invalidate(); } // Redirect to login page after logout
        response.sendRedirect("index.jsp?logout=success"); %>