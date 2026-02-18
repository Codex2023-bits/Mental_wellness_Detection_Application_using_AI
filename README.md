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
