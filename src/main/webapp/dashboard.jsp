<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Session Validation Logic
    String email = (String) session.getAttribute("user");
    
    if (email == null || email.isEmpty()) {
        response.sendRedirect("index.jsp?error=session");
        return;
    }
%>
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
                        <h1><i class="fa-solid fa-chart-line" style="margin-right: 12px;"></i>Your Dashboard</h1>
                        
                        <p class="welcome-message">
                            Hello, <span class="user-email"><%= email %></span>! Here's your wellness overview.
                        </p>

                        <div style="margin-top: 32px; display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 16px;">
                            <div style="padding: 20px; background: rgba(155, 135, 245, 0.1); border-radius: 12px;">
                                <i class="fa-solid fa-heart" style="font-size: 2rem; color: #8b5cf6;"></i>
                                <p style="margin-top: 12px; font-weight: 500;">Mood Tracker</p>
                            </div>
                            <div style="padding: 20px; background: rgba(125, 211, 192, 0.1); border-radius: 12px;">
                                <i class="fa-solid fa-brain" style="font-size: 2rem; color: #10b981;"></i>
                                <p style="margin-top: 12px; font-weight: 500;">Meditation</p>
                            </div>
                            <div style="padding: 20px; background: rgba(110, 231, 183, 0.1); border-radius: 12px;">
                                <i class="fa-solid fa-star" style="font-size: 2rem; color: #f59e0b;"></i>
                                <p style="margin-top: 12px; font-weight: 500;">Goals</p>
                            </div>
                        </div>

                        <div style="display: flex; gap: 12px; justify-content: center; margin-top: 32px;">
                            <a href="welcome.jsp" class="btn btn-primary"><i class="fa-solid fa-home"></i> Home</a>
                            <a href="profile.jsp" class="btn btn-primary"><i class="fa-solid fa-user"></i> Profile</a>
                            <a href="logout.jsp" class="btn btn-danger"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>