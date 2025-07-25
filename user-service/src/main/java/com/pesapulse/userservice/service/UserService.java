package com.pesapulse.userservice.service;

import com.pesapulse.userservice.model.User;
import com.pesapulse.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class containing the core business logic for user management.
 * It acts as an intermediary between the controller and the repository.
 */
@Service // Corrected annotation
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection.
     * @param userRepository The repository for user data access.
     * @param passwordEncoder The encoder for hashing passwords.
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     *
     * @param user The user object containing registration details.
     * @return The saved user object.
     * @throws IllegalStateException if the email is already taken.
     */
    public User registerUser(User user) {
        // 1. Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken: " + user.getEmail());
        }

        // 2. Encode the password before saving - CRITICAL SECURITY STEP
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 3. Save the new user to the database
        return userRepository.save(user);
    }
}