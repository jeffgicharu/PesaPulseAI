package com.pesapulse.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration class for the User Service.
 * This class is responsible for defining security-related beans.
 */
 @Configuration
public class SecurityConfig {

    /**
     * Creates a PasswordEncoder bean to be used for hashing passwords.
     * We use BCrypt, which is the industry standard for password storage.
     * This bean will be available for dependency injection throughout the application.
     *
     * @return An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}