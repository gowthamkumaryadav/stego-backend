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
    
    @GetMapping("/")
    public String home() {
        System.out.println("🔥 Backend wake-up ping received");
        return "Backend is running";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        try {
            System.out.println("🔥 Login request received");
            System.out.println("👉 Entered Username: " + user.getUsername());
            System.out.println("👉 Entered Password: " + user.getPassword());

            User existingUser = repo.findByUsername(user.getUsername());

            if (existingUser == null) {
                System.out.println("❌ User NOT found in DB");
                return ResponseEntity.status(401).body("User not found");
            }

            System.out.println("✅ DB Username: " + existingUser.getUsername());
            System.out.println("✅ DB Password: " + existingUser.getPassword());

            if (!existingUser.getPassword().equals(user.getPassword())) {
                System.out.println("❌ Password mismatch");
                return ResponseEntity.status(401).body("Invalid Credentials");
            }

            System.out.println("✅ Login SUCCESS");
            return ResponseEntity.ok(existingUser);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }
    }
}