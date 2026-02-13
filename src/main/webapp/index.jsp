<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Welcome - Mental Wellness</title>

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
                    <div class="col-12 text-center mb-4">
                        <h1>Welcome Back</h1>
                        <p class="subtitle">Take a moment for yourself. Your wellness journey continues here.</p>

                        <% String error=request.getParameter("error"); String logout=request.getParameter("logout"); if
                            ("session".equals(error)) { %>
                            <div
                                style="padding: 12px; background: rgba(239, 68, 68, 0.1); border-left: 4px solid #ef4444; border-radius: 8px; margin-bottom: 16px;">
                                <p style="margin: 0; color: #dc2626; font-size: 0.9rem;">
                                    <i class="fa-solid fa-exclamation-circle" style="margin-right: 8px;"></i>
                                    Your session has expired. Please login again.
                                </p>
                            </div>
                            <% } else if ("invalid".equals(error)) { %>
                                <div
                                    style="padding: 12px; background: rgba(239, 68, 68, 0.1); border-left: 4px solid #ef4444; border-radius: 8px; margin-bottom: 16px;">
                                    <p style="margin: 0; color: #dc2626; font-size: 0.9rem;">
                                        <i class="fa-solid fa-exclamation-circle" style="margin-right: 8px;"></i>
                                        Please enter valid credentials.
                                    </p>
                                </div>
                                <% } else if ("success".equals(logout)) { %>
                                    <div
                                        style="padding: 12px; background: rgba(16, 185, 129, 0.1); border-left: 4px solid #10b981; border-radius: 8px; margin-bottom: 16px;">
                                        <p style="margin: 0; color: #059669; font-size: 0.9rem;">
                                            <i class="fa-solid fa-check-circle" style="margin-right: 8px;"></i>
                                            You have been successfully logged out.
                                        </p>
                                    </div>
                                    <% } %>
                    </div>

                    <div class="col-12 d-flex flex-row justify-content-center">
                        <div class="d-flex flex-column justify-content-center glass_box">
                            <form action="login-process.jsp" method="post">
                                <div class="input_box d-flex justify-content-center mt-3">
                                    <i class="fa-solid fa-envelope icon"></i>
                                    <input type="email" name="email" class="form-control text_box"
                                        placeholder="Your email" aria-label="Email address" required>

                                </div>

                                <div class="input_box d-flex justify-content-center mt-3">
                                    <i class="fa-solid fa-lock icon"></i>
                                    <input type="password" name="password" class="form-control text_box"
                                        placeholder="Your password" aria-label="Password" required>
                                </div>
                                <div class="d-flex justify-content-center mt-4">
                                    <button type="submit" class="btn btn-primary"
                                        aria-label="Sign in to your account">Continue</button>
                                </div>
                            </form>
                        </div>
                        <div>
                            <img src="https://i.ibb.co/rKDwp3Wg/Chat-GPT-Image-Feb-11-2026-02-26-14-PM.png"
                                class="logo">
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </body>

    </html>