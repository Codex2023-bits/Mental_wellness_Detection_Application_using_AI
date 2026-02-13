<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Step 5: Retrieve the username (email) from the request object using POST method
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    
    // Basic validation
    if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
        // Step 6: Create a user session using the JSP session object (implicit object)
        // Step 7: Store the username in the session using session.setAttribute()
        session.setAttribute("user", email);
        session.setAttribute("loginTime", new java.util.Date().toString());
        
        // Step 8: Redirect the user to another JSP page after successful login
        response.sendRedirect("welcome.jsp");
    } else {
        // If validation fails, redirect back to login with error
        response.sendRedirect("index.jsp?error=invalid");
    }
%>