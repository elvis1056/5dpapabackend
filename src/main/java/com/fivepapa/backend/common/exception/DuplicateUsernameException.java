package com.fivepapa.backend.common.exception;

/**
 * Exception thrown when attempting to register with an already existing username
 */
public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username already exists: " + username);
    }
}
