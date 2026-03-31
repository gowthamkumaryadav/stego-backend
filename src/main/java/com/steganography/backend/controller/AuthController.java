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
            System.out.println("🔥 Login request received");

            // ✅ Print input from frontend
            System.out.println("👉 Entered Username: " + user.getUsername());
            System.out.println("👉 Entered Password: " + user.getPassword());

            if (user.getUsername() == null || user.getPassword() == null) {
                System.out.println("❌ Username or Password is NULL");
                return ResponseEntity.badRequest().body("Username or Password missing");
            }

            // ✅ Fetch from DB
            User existingUser = repo.findByUsername(user.getUsername());

            // ✅ Debug DB result
            if (existingUser != null) {
                System.out.println("✅ User found in DB");
                System.out.println("👉 DB Username: " + existingUser.getUsername());
                System.out.println("👉 DB Password: " + existingUser.getPassword());
            } else {
                System.out.println("❌ User NOT found in DB");
            }

            // ✅ Check user exists
            if (existingUser == null) {
                return ResponseEntity.status(401).body("User not found");
            }

            // ✅ Check password
            if (!existingUser.getPassword().equals(user.getPassword())) {
                System.out.println("❌ Password mismatch");
                return ResponseEntity.status(401).body("Invalid Credentials");
            }

            // ✅ Success
            System.out.println("✅ Login SUCCESS");
            return ResponseEntity.ok(existingUser);

        } catch (Exception e) {
            System.out.println("💥 Exception occurred in login");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}