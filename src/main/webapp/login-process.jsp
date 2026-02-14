<%@ page import="java.time.LocalDateTime" %>
    <%@ page import="java.time.format.DateTimeFormatter" %>
        <% String email=request.getParameter("email"); String pass=request.getParameter("password"); // Hardcoded
            credentials for the demo // Email: babishake8@gmail.com // Password: password if
            ("babishake8@gmail.com".equals(email) && "password" .equals(pass)) { // Login Success: Create Session
            session.setAttribute("user", email); // Store login time for the profile page DateTimeFormatter
            dtf=DateTimeFormatter.ofPattern("HH:mm:ss"); session.setAttribute("loginTime",
            LocalDateTime.now().format(dtf)); // Redirect to Welcome Page response.sendRedirect("welcome.jsp"); } else {
            // Login Failed: Redirect back with error parameter response.sendRedirect("index.jsp?error=invalid"); } %>