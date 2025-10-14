package com.fivepapa.backend.member.service;

import com.fivepapa.backend.common.exception.UserNotFoundException;
import com.fivepapa.backend.member.entity.User;
import com.fivepapa.backend.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserService
 * Handles user management operations
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Get user by ID
     * @param userId the user ID
     * @return the user
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * Get user by username
     * @param username the username
     * @return the user
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    /**
     * Get all users
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all enabled users
     * @return list of enabled users
     */
    @Transactional(readOnly = true)
    public List<User> getEnabledUsers() {
        return userRepository.findByEnabled(true);
    }

    /**
     * Update user enabled status
     * @param userId the user ID
     * @param enabled the enabled status
     * @return the updated user
     * @throws UserNotFoundException if user not found
     */
    @Transactional
    public User updateUserStatus(Long userId, Boolean enabled) {
        User user = getUserById(userId);
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    /**
     * Delete user by ID
     * @param userId the user ID
     * @throws UserNotFoundException if user not found
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
    }
}
