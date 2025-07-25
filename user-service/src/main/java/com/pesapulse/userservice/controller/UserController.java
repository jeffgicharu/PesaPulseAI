package com.pesapulse.userservice.controller;

import com.pesapulse.userservice.dto.LoginRequestDTO;
import com.pesapulse.userservice.dto.LoginResponseDTO;
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
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser( @Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(registeredUser.getId());
        responseDTO.setName(registeredUser.getName());
        responseDTO.setEmail(registeredUser.getEmail());

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Endpoint for authenticating a user and returning a JWT.
     *
     * @param loginRequest DTO containing the user's credentials.
     * @return A ResponseEntity containing the LoginResponseDTO with the JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser( @Valid @RequestBody LoginRequestDTO loginRequest) {
        // 1. Authenticate the user
        User authenticatedUser = userService.loginUser(loginRequest);

        // 2. Generate the JWT for the authenticated user
        String jwt = userService.generateJwtForUser(authenticatedUser);

        // 3. Create the response DTO
        LoginResponseDTO loginResponse = new LoginResponseDTO(
            jwt,
            "Bearer",
            authenticatedUser.getId(),
            authenticatedUser.getName()
        );

        return ResponseEntity.ok(loginResponse);
    }
}