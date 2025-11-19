package com.skybook.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

/**
 * CreditCardPayment - implements Payment interface
 * Demonstrates POLYMORPHISM
 */
@Data
@AllArgsConstructor
public class CreditCardPayment implements Payment {
    
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private String transactionId;
    
    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public boolean processPayment(Double amount) {
        if (!validatePaymentDetails()) {
            return false;
        }
        // Simulate payment processing
        this.transactionId = "CC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("Processing Credit Card payment of ₹" + amount);
        return true;
    }
    
    @Override
    public boolean validatePaymentDetails() {
        return cardNumber != null && cardNumber.length() == 16 &&
               cvv != null && cvv.length() >= 3 &&
               expiryDate != null && expiryDate.matches("\\d{2}/\\d{2}");
    }
    
    @Override
    public String getPaymentMethod() {
        return "CREDIT_CARD";
    }
    
    @Override
    public Double calculateProcessingFee(Double amount) {
        // 2% processing fee for credit cards
        return amount * 0.02;
    }
    
    @Override
    public String getTransactionId() {
        return transactionId;
    }
}