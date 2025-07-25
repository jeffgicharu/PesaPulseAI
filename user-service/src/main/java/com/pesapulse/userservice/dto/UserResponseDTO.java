package com.pesapulse.userservice.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for sending user information in API responses.
 * This class ensures that sensitive information like the password hash is never exposed to the client.
 */
 @Data // Lombok annotation to generate getters, setters, toString, etc.
public class UserResponseDTO {

    private String id;
    private String name;
    private String email;

}
