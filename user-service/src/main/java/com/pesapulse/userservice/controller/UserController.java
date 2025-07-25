package com.pesapulse.userservice.controller;

import com.pesapulse.userservice.dto.UserResponseDTO;
import com.pesapulse.userservice.model.User;
import com.pesapulse.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling user-related API requests.
 */
 @RestController @RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for registering a new user.
     * It now returns a UserResponseDTO to avoid exposing the password hash.
     *
     * @param user The user object from the request body. The @Valid annotation triggers validation.
     * @return A ResponseEntity containing the UserResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser( @Valid @RequestBody User user) {
        // The service layer still works with the internal User entity
        User registeredUser = userService.registerUser(user);

        // --- DTO CONVERSION ---
        // We create a DTO to control what data is sent back to the client.
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(registeredUser.getId());
        responseDTO.setName(registeredUser.getName());
        responseDTO.setEmail(registeredUser.getEmail());
        // Note: The password is NOT copied to the DTO.

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}