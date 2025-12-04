package com.fivepapa.backend.common.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides public endpoints for checking application status
 */
@RestController
public class HealthController {

    @Value("${spring.application.name:5dpapa-backend}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    /**
     * Root endpoint - welcomes users and shows available public endpoints
     * GET /
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to " + applicationName + " API");
        response.put("status", "running");
        response.put("timestamp", LocalDateTime.now());
        response.put("profile", activeProfile);

        // List of public endpoints
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("register", "/api/auth/register");
        endpoints.put("login", "/api/auth/login");
        endpoints.put("csrf", "/api/csrf");

        // Add H2 console link in development mode
        if ("dev".equals(activeProfile)) {
            endpoints.put("h2-console", "/h2-console");
        }

        response.put("endpoints", endpoints);

        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint - returns simple UP status
     * GET /health
     *
     * Used by:
     * - Monitoring systems (Prometheus, Grafana)
     * - Load balancers (AWS ELB, Nginx)
     * - Kubernetes liveness/readiness probes
     * - CI/CD pipelines
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
