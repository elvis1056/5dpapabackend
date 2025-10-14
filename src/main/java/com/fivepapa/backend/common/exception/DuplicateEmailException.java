package com.fivepapa.backend.common.exception;

/**
 * Exception thrown when attempting to register with an already existing email
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Email already exists: " + email);
    }
}
