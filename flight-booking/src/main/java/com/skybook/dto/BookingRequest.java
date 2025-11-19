package com.skybook.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class BookingRequest {
    @NotNull(message = "Flight ID is required")
    private Long flightId;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone must be 10 digits")
    private String phone;
    
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    private String cardNumber;
    
    @NotBlank(message = "Card expiry is required")
    @Pattern(regexp = "^\\d{2}/\\d{2}$", message = "Invalid expiry format (MM/YY)")
    private String cardExpiry;
    
    @NotBlank(message = "CVV is required")
    @Size(min = 3, max = 4, message = "CVV must be 3-4 digits")
    private String cardCvv;
    
    private String paymentType = "CREDIT_CARD"; // CREDIT_CARD, DEBIT_CARD, UPI
}