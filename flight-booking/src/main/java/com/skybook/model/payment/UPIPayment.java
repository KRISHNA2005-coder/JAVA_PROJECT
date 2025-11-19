package com.skybook.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

/**
 * UPIPayment - implements Payment interface with UPI specific logic
 * Demonstrates POLYMORPHISM - same interface, unique implementation
 */
@Data
@AllArgsConstructor
public class UPIPayment implements Payment {
    
    private String upiId;
    private String mobileNumber;
    private String transactionId;
    
    public UPIPayment(String upiId, String mobileNumber) {
        this.upiId = upiId;
        this.mobileNumber = mobileNumber;
    }
    
    @Override
    public boolean processPayment(Double amount) {
        if (!validatePaymentDetails()) {
            return false;
        }
        // Simulate UPI payment processing
        this.transactionId = "UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("Processing UPI payment of ₹" + amount + " to " + upiId);
        return true;
    }
    
    @Override
    public boolean validatePaymentDetails() {
        return upiId != null && upiId.contains("@") &&
               mobileNumber != null && mobileNumber.length() == 10;
    }
    
    @Override
    public String getPaymentMethod() {
        return "UPI";
    }
    
    @Override
    public Double calculateProcessingFee(Double amount) {
        // 0% processing fee for UPI (government initiative)
        return 0.0;
    }
    
    @Override
    public String getTransactionId() {
        return transactionId;
    }
}