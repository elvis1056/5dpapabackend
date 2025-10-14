package com.fivepapa.backend.member.service;

import com.fivepapa.backend.common.exception.DuplicateEmailException;
import com.fivepapa.backend.common.exception.DuplicateUsernameException;
import com.fivepapa.backend.common.exception.InvalidCredentialsException;
import com.fivepapa.backend.common.util.JwtUtil;
import com.fivepapa.backend.member.dto.LoginRequest;
import com.fivepapa.backend.member.dto.LoginResponse;
import com.fivepapa.backend.member.dto.RegisterRequest;
import com.fivepapa.backend.member.entity.User;
import com.fivepapa.backend.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService
 * Handles user authentication and registration
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Register a new user
     * @param request registration request with user details
     * @return login response with JWT tokens
     * @throws DuplicateUsernameException if username already exists
     * @throws DuplicateEmailException if email already exists
     */
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new DuplicateUsernameException(request.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(User.UserRole.USER)
                .enabled(true)
                .build();

        // Save user
        User savedUser = userRepository.save(user);

        // Generate tokens
        String accessToken = jwtUtil.generateToken(savedUser);
        String refreshToken = jwtUtil.generateRefreshToken(savedUser);

        // Return response
        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    /**
     * Authenticate user and generate tokens
     * @param request login request with username and password
     * @return login response with JWT tokens
     * @throws InvalidCredentialsException if credentials are invalid
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        // Check if password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // Check if user is enabled
        if (!user.getEnabled()) {
            throw new InvalidCredentialsException("Account is disabled");
        }

        // Generate tokens
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // Return response
        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /**
     * Refresh access token using refresh token
     * @param refreshToken the refresh token
     * @return login response with new JWT tokens
     * @throws InvalidCredentialsException if refresh token is invalid
     */
    @Transactional(readOnly = true)
    public LoginResponse refreshToken(String refreshToken) {
        // Validate refresh token
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        // Extract username from refresh token
        String username = jwtUtil.extractUsername(refreshToken);

        // Find user
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        // Check if user is enabled
        if (!user.getEnabled()) {
            throw new InvalidCredentialsException("Account is disabled");
        }

        // Generate new tokens
        String newAccessToken = jwtUtil.generateToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        // Return response
        return LoginResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
