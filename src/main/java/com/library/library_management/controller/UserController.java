package com.library.library_management.controller;

import com.library.library_management.model.UserDTO;
import com.library.library_management.model.User;
import com.library.library_management.patterns.UserFactory;
import com.library.library_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        try {
            System.out.println("Received DTO - Name: " + userDTO.getName() + ", Email: " + userDTO.getEmail() + ", Type: " + userDTO.getUserType());
            User user = UserFactory.createUser(userDTO.getUserType(), 0, userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        try {
            return userService.getUserById(id)
                    .map(user -> {
                        user.setName(userDTO.getName());
                        user.setEmail(userDTO.getEmail());
                        user.setPassword(userDTO.getPassword());
                        User updatedUser = userService.updateUser(user);
                        return ResponseEntity.ok(updatedUser);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // new route /me for login
    @PutMapping("/me")
    public ResponseEntity<User> updateOwnUser(@RequestBody UserDTO userDTO, Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserByEmail(email)
                .map(user -> {
                    if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
                        user.setName(userDTO.getName());
                    }
                    if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                        user.setEmail(userDTO.getEmail());
                    }
                    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                        user.setPassword(userDTO.getPassword());
                    }
                    User updatedUser = userService.updateUser(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}