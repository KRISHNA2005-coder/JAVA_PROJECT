package com.skybook.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

/**
 * DebitCardPayment - implements Payment interface differently
 * Demonstrates POLYMORPHISM - same interface, different implementation
 */
@Data
@AllArgsConstructor
public class DebitCardPayment implements Payment {
    
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private String bankName;
    private String transactionId;
    
    public DebitCardPayment(String cardNumber, String cardHolderName, 
                           String expiryDate, String cvv, String bankName) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.bankName = bankName;
    }
    
    @Override
    public boolean processPayment(Double amount) {
        if (!validatePaymentDetails()) {
            return false;
        }
        // Simulate debit card payment processing
        this.transactionId = "DC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("Processing Debit Card payment of ₹" + amount + " via " + bankName);
        return true;
    }
    
    @Override
    public boolean validatePaymentDetails() {
        return cardNumber != null && cardNumber.length() == 16 &&
               cvv != null && cvv.length() >= 3 &&
               expiryDate != null && expiryDate.matches("\\d{2}/\\d{2}") &&
               bankName != null && !bankName.isEmpty();
    }
    
    @Override
    public String getPaymentMethod() {
        return "DEBIT_CARD";
    }
    
    @Override
    public Double calculateProcessingFee(Double amount) {
        // 1% processing fee for debit cards (lower than credit)
        return amount * 0.01;
    }
    
    @Override
    public String getTransactionId() {
        return transactionId;
    }
}