package com.fivepapa.backend.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginResponse DTO
 * Used for returning authentication tokens to the client
 * Note: refreshToken is stored in HttpOnly cookie, not in response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't serialize null fields
public class LoginResponse {

    private String token;
    private String refreshToken; // Set to null before returning (stored in HttpOnly cookie)
    private String username;
    private String email;
    private String role;
}
