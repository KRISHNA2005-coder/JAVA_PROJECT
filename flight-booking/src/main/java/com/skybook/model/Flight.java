package com.skybook.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract Flight class demonstrating INHERITANCE and POLYMORPHISM
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DomesticFlight.class, name = "DOMESTIC"),
    @JsonSubTypes.Type(value = InternationalFlight.class, name = "INTERNATIONAL")
})
@Entity
@Table(name = "flights")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "flight_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Flight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "flight_number", unique = true, nullable = false)
    private String flightNumber;
    
    @Column(nullable = false)
    private String airline;
    
    @Column(name = "departure_city", nullable = false)
    private String departureCity;
    
    @Column(name = "arrival_city", nullable = false)
    private String arrivalCity;
    
    @Column(name = "departure_time")
    private String departureTime;
    
    @Column(name = "arrival_time")
    private String arrivalTime;
    
    private String duration;
    
    @Column(name = "base_price")
    private Double basePrice;
    
    @Column(name = "available_seats")
    private Integer availableSeats;
    
    private String image;
    
    // Abstract methods - must be implemented by child classes
    public abstract String getFlightType();
    public abstract Double calculateFinalPrice();
    public abstract boolean requiresVisa();
    
    // Polymorphic method - can be overridden
    public String getFlightInfo() {
        return airline + " - " + flightNumber + " (" + departureCity + " → " + arrivalCity + ")";
    }
    
    // Common behavior
    public boolean isAvailable() {
        return availableSeats != null && availableSeats > 0;
    }
    
    public void bookSeat() {
        if (isAvailable()) {
            availableSeats--;
        }
    }
}