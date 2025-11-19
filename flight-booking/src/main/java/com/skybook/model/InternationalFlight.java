package com.skybook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * InternationalFlight - demonstrates INHERITANCE and POLYMORPHISM
 */
@Entity
@DiscriminatorValue("INTERNATIONAL")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class InternationalFlight extends Flight {
    
    @Column(name = "international_tax_percentage")
    private Double internationalTaxPercentage = 12.0; // 12% tax
    
    @Column(name = "visa_required")
    private Boolean visaRequired = true;
    
    @Column(name = "baggage_allowance_kg")
    private Integer baggageAllowanceKg = 30;
    
    @Column(name = "destination_country")
    private String destinationCountry;
    
    @Override
    public String getFlightType() {
        return "INTERNATIONAL";
    }
    
    @Override
    public Double calculateFinalPrice() {
        // Add international tax and baggage fee
        double tax = getBasePrice() * (internationalTaxPercentage / 100);
        double baggageFee = 500.0; // Fixed baggage fee
        return getBasePrice() + tax + baggageFee;
    }
    
    @Override
    public boolean requiresVisa() {
        return visaRequired;
    }
    
    @Override
    public String getFlightInfo() {
        return super.getFlightInfo() + " [International to " + destinationCountry + "]";
    }
}