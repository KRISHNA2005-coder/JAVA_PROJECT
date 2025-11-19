package com.skybook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Passenger class - demonstrates INHERITANCE from User
 * Specific type of user who can book flights
 */
@Entity
@DiscriminatorValue("PASSENGER")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Passenger extends User {
    
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;
    
    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
    
    @Override
    public String getUserRole() {
        return "PASSENGER";
    }
    
    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }
    
    // Passenger specific behavior
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
    
    public boolean canBookFlight() {
        return this.getEmail() != null && !this.getEmail().isEmpty();
    }
}