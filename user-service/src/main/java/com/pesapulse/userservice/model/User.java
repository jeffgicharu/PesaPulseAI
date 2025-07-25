package com.pesapulse.userservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a User entity in the application.
 * This class now implements UserDetails to integrate with Spring Security.
 */
 @Data @Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Indexed(unique = true)
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    // --- UserDetails Methods ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // For now, we are not using roles. Return an empty list.
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        // Our "username" is the user's email address.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account is always considered non-expired.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is always considered non-locked.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials are always considered non-expired.
    }

    @Override
    public boolean isEnabled() {
        return true; // Account is always considered enabled.
    }
}