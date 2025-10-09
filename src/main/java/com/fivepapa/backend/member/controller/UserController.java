package com.fivepapa.backend.member.controller;

import com.fivepapa.backend.member.entity.User;
import com.fivepapa.backend.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 * REST API endpoints for user management
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get user by ID
     * GET /api/users/{id}
     * @param id the user ID
     * @return the user
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get all users (admin only)
     * GET /api/users
     * @return list of all users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get all enabled users (admin only)
     * GET /api/users/enabled
     * @return list of enabled users
     */
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getEnabledUsers() {
        List<User> users = userService.getEnabledUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Update user status (admin only)
     * PATCH /api/users/{id}/status
     * @param id the user ID
     * @param enabled the enabled status
     * @return the updated user
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserStatus(@PathVariable Long id, @RequestParam Boolean enabled) {
        User user = userService.updateUserStatus(id, enabled);
        return ResponseEntity.ok(user);
    }

    /**
     * Delete user (admin only)
     * DELETE /api/users/{id}
     * @param id the user ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
