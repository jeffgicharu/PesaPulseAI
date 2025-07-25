package com.pesapulse.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for sending a successful login response.
 * It contains the authentication token and essential user details for the client.
 */
 @Data @NoArgsConstructor @AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String userId;
    private String name;

}