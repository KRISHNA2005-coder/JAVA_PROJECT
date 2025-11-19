package com.skybook.controller;

import com.skybook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * UserController - REST API endpoints for user profile
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * GET /api/users/profile
     * Get user profile by email
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestParam String email) {
        return userService.getUserProfile(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * PUT /api/users/profile
     * Update user profile
     */
    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestParam String email,
            @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = userService.updateProfile(email, updates);
        return ResponseEntity.ok(response);
    }
    
    /**
     * PUT /api/users/location
     * Update user location
     */
    @PutMapping("/location")
    public ResponseEntity<Map<String, Object>> updateLocation(
            @RequestParam String email,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = userService.updateLocation(email, request.get("location"));
        return ResponseEntity.ok(response);
    }
}