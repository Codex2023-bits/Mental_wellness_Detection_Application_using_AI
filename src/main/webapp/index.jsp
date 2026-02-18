<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Mental Wellness</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="sample.css">
</head>

<body>
    <div class="background_image">
        <div class="container">
            <div class="row">
                <div class="col-12 d-flex flex-column justify-content-center align-items-center mt-5">
                    <div class="glass_box text-center" style="max-width: 400px; padding: 40px;">

                        <div style="margin-bottom: 20px;">
                            <i class="fa-solid fa-leaf" style="font-size: 3rem; color: #10b981;"></i>
                        </div>
                        <h2 style="font-weight: 600; margin-bottom: 20px; color: #374151;">Wellness Login</h2>

                        <% 
                            String error = request.getParameter("error"); 
                            if ("session".equals(error)) { 
                        %>
                            <div class="alert alert-warning" role="alert" style="font-size: 0.9rem;">
                                Session expired. Please login again.
                            </div>
                        <% 
                            } else if ("invalid".equals(error)) { 
                        %>
                            <div class="alert alert-danger" role="alert" style="font-size: 0.9rem;">
                                Invalid email or password.
                            </div>
                        <% 
                            } 
                        %>

                        <form action="login" method="POST">
                            <div class="form-group text-left">
                                <label style="font-weight: 500; color: #4b5563;">Email Address</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                                    </div>
                                    <input type="email" name="email" class="form-control"
                                        placeholder="Enter your email" required>
                                </div>
                            </div>

                            <div class="form-group text-left">
                                <label style="font-weight: 500; color: #4b5563;">Password</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                    </div>
                                    <input type="password" name="password" class="form-control"
                                        placeholder="Enter password" required>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary btn-block mt-4"
                                style="background: linear-gradient(135deg, #10b981, #059669); border: none; padding: 12px;">
                                Sign In
                            </button>
                        </form>

                        <p class="mt-3" style="font-size: 0.85rem; color: #6b7280;">
                            Demo Account: babishake8@gmail.com / password
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

</html>