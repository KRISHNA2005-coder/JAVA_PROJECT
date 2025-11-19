package com.skybook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Booking entity demonstrating relationships and encapsulation
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "booking_reference", unique = true, nullable = false)
    private String bookingReference;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
    
    @Column(name = "booking_date")
    private LocalDateTime bookingDate;
    
    @Column(nullable = false)
    private String status; // CONFIRMED, CANCELLED, COMPLETED
    
    @Column(name = "total_amount")
    private Double totalAmount;
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    // Passenger details at time of booking
    @Column(name = "passenger_first_name")
    private String passengerFirstName;
    
    @Column(name = "passenger_last_name")
    private String passengerLastName;
    
    @Column(name = "passenger_email")
    private String passengerEmail;
    
    @Column(name = "passenger_phone")
    private String passengerPhone;
    
    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
        if (status == null) {
            status = "CONFIRMED";
        }
    }
    
    // Business logic - encapsulated behavior
    public boolean canBeCancelled() {
        return "CONFIRMED".equals(status);
    }
    
    public void cancel() {
        if (canBeCancelled()) {
            this.status = "CANCELLED";
        }
    }
    
    public boolean isActive() {
        return "CONFIRMED".equals(status);
    }
}