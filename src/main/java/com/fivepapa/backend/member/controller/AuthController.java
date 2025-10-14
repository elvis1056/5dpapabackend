package com.fivepapa.backend.member.controller;

import com.fivepapa.backend.member.dto.LoginRequest;
import com.fivepapa.backend.member.dto.LoginResponse;
import com.fivepapa.backend.member.dto.RegisterRequest;
import com.fivepapa.backend.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 * REST API endpoints for authentication (register, login, refresh)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user
     * POST /api/auth/register
     * @param request registration request with user details
     * @return login response with JWT tokens
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login with username and password
     * POST /api/auth/login
     * @param request login request with credentials
     * @return login response with JWT tokens
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh access token
     * POST /api/auth/refresh
     * @param refreshToken the refresh token from Authorization header
     * @return login response with new JWT tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        // Remove "Bearer " prefix if present
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;
        LoginResponse response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }
}
