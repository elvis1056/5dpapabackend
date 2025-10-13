package com.fivepapa.backend.common.security;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UserPrincipal
 * Custom principal object containing user information from JWT
 */
@Data
@AllArgsConstructor
public class UserPrincipal {

    private Long id;
    private String username;
    private String email;
    private String role;
}
