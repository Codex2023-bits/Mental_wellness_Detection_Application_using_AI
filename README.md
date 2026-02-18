<<<<<<< HEAD
# 🚀 Updated Quick Start Guide

## Running the Application

### ⚡ Start Command (Updated for Tomcat 10)
```powershell
# Navigate to project directory
cd "c:\PBL Projects\TASK3\demo"

# Run the application
.\mvnw.cmd cargo:run
```

The application will start on: **http://localhost:8080/demo**

---

## 📝 Login Credentials

- **Email**: `babishake8@gmail.com`
- **Password**: `password`

---

## 🔗 Available Endpoints

| Endpoint | Description |
|----------|-------------|
| `/demo/` | Login page (index.jsp) |
| `/demo/login` | Login servlet |
| `/demo/dashboard.jsp` | User dashboard (requires login) |
| `/demo/welcome.jsp` | Welcome page (requires login) |
| `/demo/profile.jsp` | User profile (requires login) |
| `/demo/dbtest` | Database connection test |
| `/demo/hello-servlet` | Hello World servlet |
| `/demo/logout` | Logout servlet |

---

## 🛑 Stopping the Server

Press `Ctrl + C` in the terminal where the server is running.

---

## 🔧 Common Tasks

### Compile Only
```powershell
.\mvnw.cmd clean compile
```

### Clean Build
```powershell
.\mvnw.cmd clean package
```

### Run Tests
```powershell
.\mvnw.cmd test
```

---

## ✅ What Was Fixed (Latest)

### Previous Fixes:
1. ✅ **Added H2 Database** - Database testing now works at `/demo/dbtest`
2. ✅ **Fixed Login Time** - Profile and welcome pages now show actual login time  
3. ✅ **Removed Dead Code** - Deleted unused `login-process.jsp`
4. ✅ **Standardized Configuration** - All servlets now use `@WebServlet` annotations

### Latest Fixes:
5. ✅ **Fixed JSP Formatting** - Repaired `welcome.jsp` and `profile.jsp` variable scoping
6. ✅ **Upgraded to Tomcat 10** - Changed from tomcat7-maven-plugin to Cargo with Tomcat 10 for Jakarta EE support

---

## 💡 Important Changes

### New Run Command
**Old**: `.\mvnw.cmd tomcat7:run`  
**New**: `.\mvnw.cmd cargo:run`

We upgraded from Tomcat 7 to Tomcat 10 because Jakarta Servlet 6.1.0 requires Tomcat 10+.

---

## 📌 Tips

- The application uses **session-based authentication**
- Logout is available from dashboard, welcome, and profile pages
- The UI uses modern glassmorphism design with Bootstrap 4
- All pages are protected except login and hello-servlet
- First run will download Tomcat 10 automatically (one-time setup)
=======
Here is a complete, professional README.md file for your project. You can copy and paste this directly into a file named README.md in your project's root folder.

Mental Wellness Web Application 🧠
1. Project Overview
The Mental Wellness Web Application is a secure, Jakarta EE-based platform designed to help users track their mental health journey. It provides a private, session-based environment where individuals can monitor their mood, manage stress through meditation logs, and set personal wellness goals.

This project demonstrates core web security principles, including secure user authentication, session management, and protected route access using Java Servlets and JSP.

2. Key Features
🔐 Secure Authentication: Users must log in with valid credentials to access the platform.

🛡️ Session Management:

Creates a unique HTTP Session upon login.

Tracks login time and Session ID for security auditing.

Automatically invalidates sessions on logout to prevent unauthorized access.

🚫 Access Control: Protected pages (dashboard.jsp, profile.jsp) automatically redirect unauthenticated users back to the login screen.

📊 Personal Dashboard: A visual interface for tracking:

Mood trends.

Meditation sessions.

Active wellness goals.

👤 Dynamic Profile: Displays real-time session data, including the user's email and active session status.

📱 Responsive Design: Built with Bootstrap 4 to work seamlessly on desktops, tablets, and mobile devices.

3. Technology Stack
Backend: Java 17, Jakarta Servlet 6.0

Frontend: JSP (JavaServer Pages), HTML5, CSS3, Bootstrap 4.5

Build Tool: Apache Maven

Server: Apache Tomcat 10.1 (Required for Jakarta EE support)

IDE: (Compatible with VS Code, IntelliJ IDEA, Eclipse)

4. Project Structure
Plaintext
demo/
├── pom.xml                   # Maven Configuration & Dependencies
└── src
    └── main
        ├── java              # (Optional) Backend Java Servlets
        ├── resources         # Configuration files
        └── webapp
            ├── css
            │   └── sample.css       # Custom Styles
            ├── WEB-INF
            │   └── web.xml          # Deployment Descriptor
            ├── index.jsp            # Login Page (Entry Point)
            ├── login-process.jsp    # Authentication Logic
            ├── welcome.jsp          # Landing Page (Post-Login)
            ├── dashboard.jsp        # Main User Dashboard
            ├── profile.jsp          # User Profile & Session Info
            └── logout.jsp           # Session Invalidation Logic
5. Setup & Installation
Prerequisites
Java Development Kit (JDK) 17 or higher.

Apache Maven (or use the included mvnw wrapper).

Apache Tomcat 10.1 (or any Jakarta EE 10 compatible server).

Steps to Run
Clone the Repository:

Bash
git clone [your-repository-url]
cd demo
Build the Project:
Open a terminal in the project folder and run:

Windows: .\mvnw.cmd clean package

Mac/Linux: ./mvnw clean package

Deploy to Server:

Locate the generated .war file in the target/ directory (e.g., demo-1.0-SNAPSHOT.war).

Copy this file into the webapps/ folder of your Tomcat installation.

Start the Tomcat server (bin/startup.bat or bin/startup.sh).

Access the Application:
Open your web browser and navigate to:
http://localhost:8080/demo-1.0-SNAPSHOT/

6. Usage Guide
Login:

Email: anyone@gmail.com

Password: password

Note: These are hardcoded demo credentials.

Dashboard: After logging in, you will be redirected to welcome.jsp. Click "Dashboard" to view your stats.

Security Check: Try copying the URL of the dashboard and opening it in a different browser (Incognito mode). You should be redirected to the login page.

Logout: Click the "Sign Out" button to destroy your session.

7. Future Enhancements
Database Integration: Connect to MySQL/PostgreSQL for persistent user storage.

Data Visualization: Implement Chart.js to visualize mood trends over time.

Password Hashing: Integrate BCrypt for secure password storage.

Sign-Up Page: Allow new users to register securely.

8. License
This project is for educational purposes as part of a Web Development curriculum.
BIT college of engineering

Developed by [Codex_23]
>>>>>>> 1ba827b39f1c65123b88e86ee659164d96e1d506
