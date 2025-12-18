package com.fivepapa.backend.config;

import com.fivepapa.backend.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

/**
 * Spring Security Configuration
 * Configures JWT authentication, CORS, CSRF protection, and authorization rules
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final Environment environment;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Check if running in development mode
        boolean isDevelopment = Arrays.asList(environment.getActiveProfiles()).contains("dev");

        // CSRF Token Handler - uses the new approach for Spring Security 6+
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Enable CSRF protection with Cookie-based token repository
                .csrf(csrf -> {
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers(
                                // Public information endpoints (read-only)
                                "/", "/health",
                                // Authentication endpoints
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/refresh",
                                "/api/auth/logout",
                                // E-commerce API (uses JWT Bearer token, doesn't need CSRF)
                                "/api/products/**",
                                "/api/categories/**",
                                "/api/cart/**"
                        );

                    // Disable CSRF for H2 Console in development
                    if (isDevelopment) {
                        csrf.ignoringRequestMatchers("/h2-console/**");
                    }
                })
                .authorizeHttpRequests(auth -> {
                    // ===== Public Information Endpoints =====
                    auth.requestMatchers("/", "/health").permitAll();

                    // ===== Authentication Endpoints =====
                    auth.requestMatchers(
                            "/api/auth/login",
                            "/api/auth/register",
                            "/api/auth/refresh",
                            "/api/auth/logout",
                            "/api/csrf"
                    ).permitAll();

                    // ===== E-commerce Product Endpoints =====
                    // Public: Browse products (GET only)
                    auth.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll();
                    // Admin only: Manage products (POST/PUT/DELETE)
                    auth.requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN");

                    // ===== E-commerce Category Endpoints =====
                    // Public: Browse categories (GET only)
                    auth.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll();
                    // Admin only: Manage categories (POST/PUT/DELETE)
                    auth.requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN");

                    // ===== E-commerce Cart Endpoints =====
                    // Authenticated users only: Manage shopping cart
                    auth.requestMatchers("/api/cart/**").authenticated();

                    // ===== Development Only Endpoints =====
                    if (isDevelopment) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    // ===== All Other Requests Require Authentication =====
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Allow H2 console frames (development only)
        if (isDevelopment) {
            http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Development: Allow all origins for testing
        // Production: Should restrict to specific domains only
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "https://elvis1056.github.io",
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Allow specific headers including CSRF token
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-XSRF-TOKEN",  // CSRF token header
                "X-Requested-With"
        ));

        // Expose CSRF token header to frontend
        configuration.setExposedHeaders(Arrays.asList("X-XSRF-TOKEN"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
