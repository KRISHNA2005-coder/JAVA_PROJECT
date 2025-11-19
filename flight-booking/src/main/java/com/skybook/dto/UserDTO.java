package com.skybook.dto;

import com.skybook.model.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // User type
    private String type; // "PASSENGER" or "ADMIN"
    
    // Passenger fields
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private Integer loyaltyPoints;
    
    // Admin fields
    private String department;
    private Integer adminLevel;
    private Boolean canManageFlights;
    private Boolean canManageUsers;
    
    public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setLocation(user.getLocation());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        if (user instanceof Passenger) {
            Passenger p = (Passenger) user;
            dto.setType("PASSENGER");
            dto.setFirstName(p.getFirstName());
            dto.setLastName(p.getLastName());
            dto.setDateOfBirth(p.getDateOfBirth());
            dto.setAddress(p.getAddress());
            dto.setLoyaltyPoints(p.getLoyaltyPoints());
        } else if (user instanceof Admin) {
            Admin a = (Admin) user;
            dto.setType("ADMIN");
            dto.setDepartment(a.getDepartment());
            dto.setAdminLevel(a.getAdminLevel());
            dto.setCanManageFlights(a.getCanManageFlights());
            dto.setCanManageUsers(a.getCanManageUsers());
        }
        
        return dto;
    }
    
    public User toUser() {
        if ("PASSENGER".equals(type)) {
            Passenger user = new Passenger();
            user.setId(id);
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setPhone(phone);
            user.setLocation(location);
            user.setCreatedAt(createdAt);
            user.setUpdatedAt(updatedAt);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setDateOfBirth(dateOfBirth);
            user.setAddress(address);
            user.setLoyaltyPoints(loyaltyPoints);
            return user;
        } else {
            Admin user = new Admin();
            user.setId(id);
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setPhone(phone);
            user.setLocation(location);
            user.setCreatedAt(createdAt);
            user.setUpdatedAt(updatedAt);
            user.setDepartment(department);
            user.setAdminLevel(adminLevel);
            user.setCanManageFlights(canManageFlights);
            user.setCanManageUsers(canManageUsers);
            return user;
        }
    }
}