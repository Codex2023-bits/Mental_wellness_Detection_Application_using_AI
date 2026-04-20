package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * Task 4 – Token Issuer
 * POST /api/token  (web-session required – user must be logged in)
 *
 * Returns a short-lived Bearer token the mobile app can use for /api/steps.
 * The mobile app should call this endpoint once (e.g. on first launch or token expiry),
 * store the token securely (Android EncryptedSharedPreferences), then attach it
 * to every /api/steps request as:
 *     Authorization: Bearer <token>
 */
@WebServlet("/api/token")
public class TokenIssuerServlet extends HttpServlet {

    private static final SecureRandom RNG = new SecureRandom();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // User must be logged in via the web session
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\":\"You must be logged in to generate a token\"}");
            return;
        }
        String userEmail = session.getAttribute("user").toString();

        // Generate a cryptographically secure 48-byte (64-char base64) token
        byte[] bytes = new byte[48];
        RNG.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Token valid for 30 days
        Timestamp expiresAt = Timestamp.from(Instant.now().plus(30, ChronoUnit.DAYS));

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO api_tokens (user_email, token, expires_at) VALUES (?, ?, ?)")) {
            ps.setString(1, userEmail);
            ps.setString(2, token);
            ps.setTimestamp(3, expiresAt);
            ps.executeUpdate();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"" + e.getMessage() + "\"}");
            return;
        }

        out.write("{\"token\":\"" + token + "\",\"expires_at\":\"" + expiresAt + "\"}");
    }
}
