package com.fivepapa.backend.member.controller;

import com.fivepapa.backend.member.dto.LoginRequest;
import com.fivepapa.backend.member.dto.LoginResponse;
import com.fivepapa.backend.member.dto.RegisterRequest;
import com.fivepapa.backend.member.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 * REST API endpoints for authentication (register, login, refresh, logout)
 * Uses HttpOnly Cookies for secure refresh token storage
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${REFRESH_TOKEN_EXPIRATION_DAYS:7}")
    private int refreshTokenExpirationDays;

    @Value("${COOKIE_SECURE:true}")
    private boolean cookieSecure;

    /**
     * Register a new user
     * POST /api/auth/register
     * @param request registration request with user details
     * @param response HTTP response to set cookies
     * @return login response with access token (refresh token in HttpOnly cookie)
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = authService.register(request);

        // Set refresh token in HttpOnly cookie
        setRefreshTokenCookie(response, loginResponse.getRefreshToken());

        // Remove refresh token from response body (security best practice)
        loginResponse.setRefreshToken(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }

    /**
     * Login with username and password
     * POST /api/auth/login
     * @param request login request with credentials
     * @param response HTTP response to set cookies
     * @return login response with access token (refresh token in HttpOnly cookie)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = authService.login(request);

        // Set refresh token in HttpOnly cookie
        setRefreshTokenCookie(response, loginResponse.getRefreshToken());

        // Remove refresh token from response body (security best practice)
        loginResponse.setRefreshToken(null);

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Refresh access token using refresh token from HttpOnly cookie
     * POST /api/auth/refresh
     * @param refreshToken the refresh token from HttpOnly cookie
     * @param response HTTP response to set new cookies
     * @return login response with new access token (new refresh token in HttpOnly cookie)
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        LoginResponse loginResponse = authService.refreshToken(refreshToken);

        // Set new refresh token in HttpOnly cookie (token rotation)
        setRefreshTokenCookie(response, loginResponse.getRefreshToken());

        // Remove refresh token from response body
        loginResponse.setRefreshToken(null);

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Logout user by clearing the refresh token cookie
     * POST /api/auth/logout
     * @param response HTTP response to clear cookies
     * @return empty response with 200 OK
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Clear refresh token cookie
        clearRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    /**
     * Helper method to set refresh token as HttpOnly cookie
     * @param response HTTP response
     * @param refreshToken refresh token string
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);        // Prevent XSS attacks
        cookie.setSecure(cookieSecure);  // Controlled by COOKIE_SECURE env var (true in prod, false in dev)
        cookie.setPath("/");
        cookie.setMaxAge(refreshTokenExpirationDays * 24 * 60 * 60); // Convert days to seconds
        cookie.setAttribute("SameSite", "None"); // Required for cross-origin requests

        response.addCookie(cookie);
    }

    /**
     * Helper method to clear refresh token cookie
     * @param response HTTP response
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);  // Match the setting in setRefreshTokenCookie
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire immediately
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }
}
