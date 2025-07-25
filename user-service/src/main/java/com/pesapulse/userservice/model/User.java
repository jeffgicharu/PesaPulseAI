package com.pesapulse.userservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Represents a User entity in the application.
 * This class is mapped to the "users" collection in the MongoDB database.
 */
 @Data // Lombok annotation to generate getters, setters, toString, etc.
 @Document(collection = "users") // Specifies the MongoDB collection name
public class User {

    @Id
    private String id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Indexed(unique = true) // Ensures email addresses are unique in the database
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
