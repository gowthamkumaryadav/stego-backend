package com.steganography.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.steganography.backend.model.User;
import com.steganography.backend.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://stego-backend-production.up.railway.app")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        repo.save(user);
        return "User registered!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        try {
            System.out.println("Login request received");

            if (user.getUsername() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("Username or Password missing");
            }

            User existingUser = repo.findByUsername(user.getUsername());

            if (existingUser == null) {
                return ResponseEntity.status(401).body("User not found");
            }

            if (!existingUser.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid Credentials");
            }

            return ResponseEntity.ok(existingUser);

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 will show error in Railway logs
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}