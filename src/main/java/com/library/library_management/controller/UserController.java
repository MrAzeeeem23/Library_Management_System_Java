package com.library.library_management.controller;

import com.library.library_management.model.UserDTO;
import com.library.library_management.model.User;
import com.library.library_management.patterns.UserFactory;
import com.library.library_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            User user = UserFactory.createUser(userDTO.getUserType(), 0, userDTO.getName(), userDTO.getEmail());
            System.out.println("Created User - Name: " + user.getName() + ", Email: " + user.getEmail());
            User savedUser = userService.saveUser(user);  // Will throw if duplicate email
            System.out.println("Saved User - Name: " + savedUser.getName() + ", Email: " + savedUser.getEmail());
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);  // Return 400 for duplicates
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        try {
            return userService.getUserById(id)
                    .map(user -> {
                        System.out.println("Updating User ID: " + id + " - New Name: " + userDTO.getName() + ", New Email: " + userDTO.getEmail());
                        user.setName(userDTO.getName());
                        user.setEmail(userDTO.getEmail());
                        User updatedUser = userService.updateUser(user);  // Use updateUser method
                        System.out.println("Updated User - Name: " + updatedUser.getName() + ", Email: " + updatedUser.getEmail());
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
}