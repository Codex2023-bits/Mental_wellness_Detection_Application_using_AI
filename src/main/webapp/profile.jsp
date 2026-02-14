<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% // Session Validation Logic 
    String email=(String) session.getAttribute("user"); 
    String loginTime=(String) session.getAttribute("loginTime"); 
    if (email==null || email.isEmpty()) 
    {
        response.sendRedirect("index.jsp?error=session"); 
        return; 
    } 
    %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Profile - Mental Wellness</title>
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link rel="stylesheet" href="sample.css">
        </head>

        <body>
            <div class="background_image">
                <div class="container">
                    <div class="row">
                        <div class="col-12 d-flex flex-column justify-content-center align-items-center mt-5">
                            <div class="glass_box text-center" style="max-width: 500px;">
                                <h1><i class="fa-solid fa-user-circle" style="margin-right: 12px;"></i>Your Profile</h1>

                                <div
                                    style="margin-top: 32px; padding: 24px; background: rgba(255, 255, 255, 0.5); border-radius: 16px; text-align: left;">
                                    <div style="margin-bottom: 16px;">
                                        <p style="font-size: 0.85rem; color: #6b7280; margin-bottom: 4px;">Email Address
                                        </p>
                                        <p style="font-weight: 500; font-size: 1.1rem;">
                                            <i class="fa-solid fa-envelope"
                                                style="margin-right: 8px; color: #8b5cf6;"></i>
                                            <%= email %>
                                        </p>
                                    </div>
                                    <div style="margin-bottom: 16px;">
                                        <p style="font-size: 0.85rem; color: #6b7280; margin-bottom: 4px;">Session
                                            Started</p>
                                        <p style="font-weight: 500;">
                                            <i class="fa-solid fa-clock" style="margin-right: 8px; color: #10b981;"></i>
                                            <%= loginTime %>
                                        </p>
                                    </div>
                                    <div>
                                        <p style="font-size: 0.85rem; color: #6b7280; margin-bottom: 4px;">Session ID
                                        </p>
                                        <p style="font-weight: 500; font-size: 0.9rem; word-break: break-all;">
                                            <i class="fa-solid fa-fingerprint"
                                                style="margin-right: 8px; color: #10b981;"></i>
                                            <%= session.getId() %>
                                        </p>
                                    </div>
                                </div>

                                <div style="display: flex; gap: 12px; justify-content: center; margin-top: 32px;">
                                    <a href="welcome.jsp" class="btn btn-primary"><i class="fa-solid fa-home"></i>
                                        Home</a>
                                    <a href="dashboard.jsp" class="btn btn-primary"><i
                                            class="fa-solid fa-chart-line"></i> Dashboard</a>
                                    <a href="logout.jsp" class="btn btn-danger"><i
                                            class="fa-solid fa-right-from-bracket"></i> Logout</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>

        </html>