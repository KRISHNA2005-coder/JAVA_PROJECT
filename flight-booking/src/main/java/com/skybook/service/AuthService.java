package com.skybook.service;

import com.skybook.dto.LoginRequest;
import com.skybook.dto.RegisterRequest;
import com.skybook.model.Passenger;
import com.skybook.model.User;
import com.skybook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AuthService - Simple authentication service
 * Demonstrates service layer pattern and business logic
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            response.put("success", false);
            response.put("message", "Email already registered");
            return response;
        }
        
        // Create new passenger (demonstrating polymorphism - saving as User)
        Passenger passenger = new Passenger();
        passenger.setEmail(request.getEmail());
        passenger.setPassword(request.getPassword()); // In production, hash this!
        passenger.setName(request.getName());
        passenger.setFirstName(request.getName().split(" ")[0]);
        passenger.setLastName(request.getName().contains(" ") ? 
            request.getName().substring(request.getName().indexOf(" ") + 1) : "");
        passenger.setLocation("All India");
        
        User savedUser = userRepository.save(passenger);
        
        response.put("success", true);
        response.put("message", "Registration successful");
        response.put("user", createUserResponse(savedUser));
        
        return response;
    }
    
    @Transactional
    public Map<String, Object> login(LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }
        
        User user = userOpt.get();
        
        // Simple password check (in production, use proper hashing!)
        if (!user.getPassword().equals(request.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid password");
            return response;
        }
        
        // Update last login
        user.updateLastLogin();
        userRepository.save(user);
        
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("user", createUserResponse(user));
        
        return response;
    }
    
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("name", user.getDisplayName()); // Polymorphic method call
        userMap.put("role", user.getUserRole()); // Abstract method implementation
        userMap.put("location", user.getLocation());
        return userMap;
    }
}