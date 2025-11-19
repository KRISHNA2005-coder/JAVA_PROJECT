package com.skybook.service;

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
 * UserService - User profile management
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> getUserProfile(String email) {
        return userRepository.findByEmail(email)
            .map(this::convertToResponse);
    }
    
    @Transactional
    public Map<String, Object> updateProfile(String email, Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }
        
        User user = userOpt.get();
        
        // Update common fields
        if (updates.containsKey("phone")) {
            user.setPhone((String) updates.get("phone"));
        }
        if (updates.containsKey("location")) {
            user.setLocation((String) updates.get("location"));
        }
        
        // If passenger, update passenger-specific fields
        if (user instanceof Passenger) {
            Passenger passenger = (Passenger) user;
            if (updates.containsKey("firstName")) {
                passenger.setFirstName((String) updates.get("firstName"));
            }
            if (updates.containsKey("lastName")) {
                passenger.setLastName((String) updates.get("lastName"));
            }
            if (updates.containsKey("address")) {
                passenger.setAddress((String) updates.get("address"));
            }
        }
        
        User savedUser = userRepository.save(user);
        
        response.put("success", true);
        response.put("message", "Profile updated successfully");
        response.put("user", convertToResponse(savedUser));
        
        return response;
    }
    
    @Transactional
    public Map<String, Object> updateLocation(String email, String location) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }
        
        User user = userOpt.get();
        user.setLocation(location);
        userRepository.save(user);
        
        response.put("success", true);
        response.put("message", "Location updated successfully");
        response.put("location", location);
        
        return response;
    }
    
    private Map<String, Object> convertToResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("name", user.getDisplayName());
        response.put("phone", user.getPhone());
        response.put("location", user.getLocation());
        response.put("role", user.getUserRole());
        
        if (user instanceof Passenger) {
            Passenger passenger = (Passenger) user;
            response.put("firstName", passenger.getFirstName());
            response.put("lastName", passenger.getLastName());
            response.put("address", passenger.getAddress());
            response.put("loyaltyPoints", passenger.getLoyaltyPoints());
        }
        
        return response;
    }
}