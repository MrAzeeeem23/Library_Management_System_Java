package com.library.library_management.controller;

import com.library.library_management.model.UserDTO;
import com.library.library_management.model.User;
import com.library.library_management.patterns.UserFactory;
import com.library.library_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        try {
            User user = UserFactory.createUser(userDTO.getUserType(), 0, userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(Authentication authentication) {
        // Authentication is handled by Spring Security; if we reach here, the user is authenticated
        return ResponseEntity.ok("Login successful for user: " + authentication.getName());
    }
}