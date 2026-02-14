<% // invalidates the session to clear all data if (session !=null) { session.invalidate(); } // redirect back to login
    page response.sendRedirect("index.jsp"); %>