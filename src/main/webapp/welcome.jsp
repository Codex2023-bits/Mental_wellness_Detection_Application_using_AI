<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% // Step 9: Retrieve the stored username using session.getAttribute() on subsequent pages // Step 14: Prevent
        access to session data after logout by checking session validity String email=(String)
        session.getAttribute("user"); String loginTime=(String) session.getAttribute("loginTime"); // Check if session
        is valid - if not, redirect to login if (email==null || email.isEmpty()) {
        response.sendRedirect("index.jsp?error=session"); return; } %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Your Wellness Space</title>
            <!-- Bootstrap -->
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
            <!-- Font Awesome -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link rel="stylesheet" href="sample.css">
        </head>

        <body>
            <div class="background_image">
                <div class="container">
                    <div class="row">
                        <div class="col-12 d-flex flex-column justify-content-center align-items-center mt-5">
                            <div class="glass_box text-center">
                                <!-- Step 10: Display the username to confirm that the session is active -->
                                <h1>Welcome Back, <span class="user-email">
                                        <%= email %>
                                    </span></h1>
                                <p class="welcome-message">We're glad you're here. Your mental wellness matters,
                                    and taking time for yourself is an important step.</p>

                                <div
                                    style="margin-top: 24px; padding: 16px; background: rgba(155, 135, 245, 0.1); border-radius: 12px;">
                                    <p style="margin: 0; font-size: 0.9rem; color: #6b7280;">
                                        <i class="fa-solid fa-clock" style="margin-right: 8px;"></i>
                                        Session started: <%= loginTime %>
                                    </p>
                                </div>

                                <!-- Step 11: Maintain the session while navigating between multiple JSP pages -->
                                <p class="welcome-message" style="margin-top: 24px; font-size: 0.9rem; color: #9ca3af;">
                                    Ready to continue your journey?
                                </p>

                                <div
                                    style="display: flex; gap: 12px; justify-content: center; margin-top: 24px; flex-wrap: wrap;">
                                    <a href="dashboard.jsp" class="btn btn-primary">
                                        <i class="fa-solid fa-chart-line" style="margin-right: 8px;"></i>Dashboard
                                    </a>
                                    <a href="profile.jsp" class="btn btn-primary">
                                        <i class="fa-solid fa-user" style="margin-right: 8px;"></i>Profile
                                    </a>
                                </div>

                                <!-- Step 12: Provide a logout option for the user -->
                                <a href="logout.jsp" class="btn btn-primary mt-4"
                                    style="background: linear-gradient(135deg, #ef4444, #dc2626);">
                                    <i class="fa-solid fa-right-from-bracket" style="margin-right: 8px;"></i>Sign Out
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>

        </html>