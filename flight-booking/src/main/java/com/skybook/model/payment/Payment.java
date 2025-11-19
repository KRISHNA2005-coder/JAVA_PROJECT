package com.skybook.model.payment;

/**
 * Payment interface demonstrating POLYMORPHISM and ABSTRACTION
 * Different payment methods will implement this interface
 */
public interface Payment {
    
    /**
     * Process payment - each payment type implements differently
     */
    boolean processPayment(Double amount);
    
    /**
     * Validate payment details
     */
    boolean validatePaymentDetails();
    
    /**
     * Get payment method name
     */
    String getPaymentMethod();
    
    /**
     * Calculate processing fee (different for each payment type)
     */
    Double calculateProcessingFee(Double amount);
    
    /**
     * Get transaction ID after successful payment
     */
    String getTransactionId();
}