package com.skybook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DomesticFlight - demonstrates INHERITANCE and POLYMORPHISM
 */
@Entity
@DiscriminatorValue("DOMESTIC")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DomesticFlight extends Flight {
    
    @Column(name = "state_tax_percentage")
    private Double stateTaxPercentage = 5.0; // 5% tax for domestic
    
    @Column(name = "includes_meal")
    private Boolean includesMeal = false;
    
    @Override
    public String getFlightType() {
        return "DOMESTIC";
    }
    
    @Override
    public Double calculateFinalPrice() {
        // Add state tax to base price
        double tax = getBasePrice() * (stateTaxPercentage / 100);
        double mealCharge = includesMeal ? 200.0 : 0.0;
        return getBasePrice() + tax + mealCharge;
    }
    
    @Override
    public boolean requiresVisa() {
        return false; // Domestic flights don't need visa
    }
    
    @Override
    public String getFlightInfo() {
        return super.getFlightInfo() + " [Domestic]";
    }
}package com.skybook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DomesticFlight - demonstrates INHERITANCE and POLYMORPHISM
 */
@Entity
@DiscriminatorValue("DOMESTIC")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DomesticFlight extends Flight {
    
    @Column(name = "state_tax_percentage")
    private Double stateTaxPercentage = 5.0; // 5% tax for domestic
    
    @Column(name = "includes_meal")
    private Boolean includesMeal = false;
    
    @Override
    public String getFlightType() {
        return "DOMESTIC";
    }
    
    @Override
    public Double calculateFinalPrice() {
        // Add state tax to base price
        double tax = getBasePrice() * (stateTaxPercentage / 100);
        double mealCharge = includesMeal ? 200.0 : 0.0;
        return getBasePrice() + tax + mealCharge;
    }
    
    @Override
    public boolean requiresVisa() {
        return false; // Domestic flights don't need visa
    }
    
    @Override
    public String getFlightInfo() {
        return super.getFlightInfo() + " [Domestic]";
    }
}