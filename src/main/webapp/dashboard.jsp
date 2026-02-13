<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% // Step 14: Prevent access to session data after logout by checking session validity String email=(String)
        session.getAttribute("user"); if (email==null || email.isEmpty()) {
        response.sendRedirect("index.jsp?error=session"); return; } %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard - Mental Wellness</title>
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link rel="stylesheet" href="sample.css">
        </head>

        <body>
            <div class="background_image">
                <div class="container">
                    <div class="row">
                        <div class="col-12 d-flex flex-column justify-content-center align-items-center mt-5">
                            <div class="glass_box text-center" style="max-width: 600px;">
                                <h1><i class="fa-solid fa-chart-line" style="margin-right: 12px;"></i>Your Dashboard
                                </h1>

                                <!-- Step 10: Display the username to confirm that the session is active -->
                                <p class="welcome-message">
                                    Hello, <span class="user-email">
                                        <%= email %>
                                    </span>! Here's your wellness overview.
                                </p>

                                <!-- Step 11: Maintain the session while navigating between multiple JSP pages -->
                                <div
                                    style="margin-top: 32px; display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 16px;">
                                    <div
                                        style="padding: 20px; background: rgba(155, 135, 245, 0.1); border-radius: 12px;">
                                        <i class="fa-solid fa-heart"
                                            style="font-size: 2rem; color: var(--primary-lavender);"></i>
                                        <p style="margin-top: 12px; font-weight: 500; color: var(--dark-text);">Mood
                                            Tracker</p>
                                        <p style="font-size: 0.85rem; color: var(--soft-gray);">Track daily</p>
                                    </div>

                                    <div
                                        style="padding: 20px; background: rgba(125, 211, 192, 0.1); border-radius: 12px;">
                                        <i class="fa-solid fa-brain"
                                            style="font-size: 2rem; color: var(--primary-mint);"></i>
                                        <p style="margin-top: 12px; font-weight: 500; color: var(--dark-text);">
                                            Meditation</p>
                                        <p style="font-size: 0.85rem; color: var(--soft-gray);">5 sessions</p>
                                    </div>

                                    <div
                                        style="padding: 20px; background: rgba(110, 231, 183, 0.1); border-radius: 12px;">
                                        <i class="fa-solid fa-star"
                                            style="font-size: 2rem; color: var(--success-green);"></i>
                                        <p style="margin-top: 12px; font-weight: 500; color: var(--dark-text);">Goals
                                        </p>
                                        <p style="font-size: 0.85rem; color: var(--soft-gray);">3 active</p>
                                    </div>
                                </div>

                                <div
                                    style="display: flex; gap: 12px; justify-content: center; margin-top: 32px; flex-wrap: wrap;">
                                    <a href="welcome.jsp" class="btn btn-primary">
                                        <i class="fa-solid fa-home" style="margin-right: 8px;"></i>Home
                                    </a>
                                    <a href="profile.jsp" class="btn btn-primary">
                                        <i class="fa-solid fa-user" style="margin-right: 8px;"></i>Profile
                                    </a>
                                    <a href="logout.jsp" class="btn btn-primary"
                                        style="background: linear-gradient(135deg, #ef4444, #dc2626);">
                                        <i class="fa-solid fa-right-from-bracket" style="margin-right: 8px;"></i>Logout
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>

        </html>