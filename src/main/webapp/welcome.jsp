<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="jakarta.servlet.http.HttpSession" %>
        <% // 5. Retrieve the username from the request object. String email=request.getParameter("email"); String
            password=request.getParameter("password"); // 6. Create a user session using the JSP session object. if
            (email !=null && !email.isEmpty()) { session.setAttribute("user", email); } else { // If accessed directly
            without login, try to get from session email=(String) session.getAttribute("user"); } %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome</title>
                <!-- Bootstrap -->
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
                <link rel="stylesheet" href="sample.css">
            </head>

            <body>
                <div class="background_image">
                    <div class="container">
                        <div class="row">
                            <div class="col-12 d-flex flex-column justify-content-center align-items-center mt-5">
                                <div class="glass_box text-center">
                                    <% if (email !=null) { %>
                                        <h1 style="color: darkslategrey;">Welcome, <%= email %>!</h1>
                                        <p class="mt-3">You have successfully logged in.</p>
                                        <a href="index.jsp" class="btn btn-primary mt-3">Logout</a>
                                        <% } else { %>
                                            <h1 style="color: darkslategrey;">Access Denied</h1>
                                            <p class="mt-3">Please login first.</p>
                                            <a href="index.jsp" class="btn btn-primary mt-3">Go to Login</a>
                                            <% } %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </body>

            </html>