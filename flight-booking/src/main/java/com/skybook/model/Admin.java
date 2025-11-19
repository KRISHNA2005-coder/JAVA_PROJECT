package com.skybook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Admin class - demonstrates INHERITANCE from User
 * Has elevated privileges
 */
@Entity
@DiscriminatorValue("ADMIN")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Admin extends User {
    
    private String department;
    
    @Column(name = "admin_level")
    private Integer adminLevel = 1;
    
    @Column(name = "can_manage_flights")
    private Boolean canManageFlights = true;
    
    @Column(name = "can_manage_users")
    private Boolean canManageUsers = false;
    
    @Override
    public String getUserRole() {
        return "ADMIN";
    }
    
    @Override
    public String getDisplayName() {
        return getName() + " (Admin)";
    }
    
    // Admin specific behavior
    public boolean hasPermission(String permission) {
        if (permission.equals("MANAGE_FLIGHTS")) {
            return canManageFlights;
        }
        if (permission.equals("MANAGE_USERS")) {
            return canManageUsers;
        }
        return false;
    }
}