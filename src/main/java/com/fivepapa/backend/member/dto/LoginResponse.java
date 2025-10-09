package com.fivepapa.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginResponse DTO
 * Used for returning authentication tokens to the client
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String refreshToken;
    private String username;
    private String email;
    private String role;
}
