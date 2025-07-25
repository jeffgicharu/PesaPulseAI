package com.pesapulse.userservice.controller;

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
 @RestController @RequestMapping("/api/users") // Maps all handler methods in this controller to the /api/users path
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for registering a new user.
     * It listens for POST requests on /api/users/register.
     *
     * @param user The user object from the request body. The @Valid annotation triggers validation.
     * @return A ResponseEntity containing the created user and HTTP status 201 (Created).
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser( @Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}