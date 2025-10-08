package com.fivepapa.backend.member.repository;

import com.fivepapa.backend.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository
 * Data access layer for User entity
 * Spring Data JPA will auto-implement these methods based on naming conventions
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username (case-sensitive)
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by username (case-insensitive)
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Find user by email (case-sensitive)
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email (case-insensitive)
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Find all users by enabled status
     * @param enabled true for enabled users, false for disabled users
     * @return List of users matching the enabled status
     */
    List<User> findByEnabled(Boolean enabled);

    /**
     * Check if username already exists (case-sensitive)
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if username already exists (case-insensitive)
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Check if email already exists (case-sensitive)
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if email already exists (case-insensitive)
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmailIgnoreCase(String email);
}
