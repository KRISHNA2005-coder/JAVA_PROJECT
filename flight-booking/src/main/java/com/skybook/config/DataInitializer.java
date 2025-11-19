package com.skybook.config;

import com.skybook.model.*;
import com.skybook.repository.FlightRepository;
import com.skybook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - Loads sample data on startup
 * Demonstrates polymorphism - creating different types of flights and users
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    
    @Override
    public void run(String... args) {
        System.out.println("\n========== Initializing Sample Data ==========\n");
        
        // Only create data if it doesn't exist
        if (flightRepository.findAll().isEmpty()) {
            // Create sample flights using polymorphism
            createDomesticFlights();
            createInternationalFlights();
        } else {
            System.out.println("✅ Flights already exist, skipping initialization");
        }
        
        if (userRepository.findAll().isEmpty()) {
            // Create sample users
            createSampleUsers();
        } else {
            System.out.println("✅ Users already exist, skipping initialization");
        }
        
        System.out.println("\n========== Sample Data Loaded Successfully ==========\n");
    }
    
    private void createDomesticFlights() {
        DomesticFlight flight1 = new DomesticFlight();
        flight1.setFlightNumber("AI101");
        flight1.setAirline("Air India");
        flight1.setDepartureCity("Delhi (DEL)");
        flight1.setArrivalCity("Mumbai (BOM)");
        flight1.setDepartureTime("6:00 AM");
        flight1.setArrivalTime("8:15 AM");
        flight1.setDuration("2h 15m");
        flight1.setBasePrice(4500.0);
        flight1.setAvailableSeats(50);
        flight1.setImage("https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=400");
        flight1.setStateTaxPercentage(5.0);
        flight1.setIncludesMeal(true);
        flightRepository.save(flight1);
        
        DomesticFlight flight2 = new DomesticFlight();
        flight2.setFlightNumber("6E202");
        flight2.setAirline("IndiGo");
        flight2.setDepartureCity("Mumbai (BOM)");
        flight2.setArrivalCity("Bangalore (BLR)");
        flight2.setDepartureTime("9:30 AM");
        flight2.setArrivalTime("11:00 AM");
        flight2.setDuration("1h 30m");
        flight2.setBasePrice(3800.0);
        flight2.setAvailableSeats(45);
        flight2.setImage("https://images.unsplash.com/photo-1464037866556-6812c9d1c72e?w=400");
        flight2.setStateTaxPercentage(5.0);
        flight2.setIncludesMeal(false);
        flightRepository.save(flight2);
        
        DomesticFlight flight3 = new DomesticFlight();
        flight3.setFlightNumber("SG303");
        flight3.setAirline("SpiceJet");
        flight3.setDepartureCity("Delhi (DEL)");
        flight3.setArrivalCity("Goa (GOI)");
        flight3.setDepartureTime("10:45 AM");
        flight3.setArrivalTime("1:15 PM");
        flight3.setDuration("2h 30m");
        flight3.setBasePrice(5200.0);
        flight3.setAvailableSeats(40);
        flight3.setImage("https://images.unsplash.com/photo-1520109344942-de335cbddc98?w=400");
        flight3.setStateTaxPercentage(5.0);
        flight3.setIncludesMeal(true);
        flightRepository.save(flight3);
        
        DomesticFlight flight4 = new DomesticFlight();
        flight4.setFlightNumber("UK404");
        flight4.setAirline("Vistara");
        flight4.setDepartureCity("Bangalore (BLR)");
        flight4.setArrivalCity("Hyderabad (HYD)");
        flight4.setDepartureTime("2:30 PM");
        flight4.setArrivalTime("3:30 PM");
        flight4.setDuration("1h 00m");
        flight4.setBasePrice(2900.0);
        flight4.setAvailableSeats(55);
        flight4.setImage("https://images.unsplash.com/photo-1583427920852-e069a2b84e8a?w=400");
        flight4.setStateTaxPercentage(5.0);
        flight4.setIncludesMeal(false);
        flightRepository.save(flight4);
        
        System.out.println("✅ Created 4 Domestic Flights");
    }
    
    private void createInternationalFlights() {
        InternationalFlight flight1 = new InternationalFlight();
        flight1.setFlightNumber("AI505");
        flight1.setAirline("Air India");
        flight1.setDepartureCity("Delhi (DEL)");
        flight1.setArrivalCity("Dubai (DXB)");
        flight1.setDepartureTime("11:00 PM");
        flight1.setArrivalTime("1:30 AM");
        flight1.setDuration("3h 30m");
        flight1.setBasePrice(18000.0);
        flight1.setAvailableSeats(30);
        flight1.setImage("https://images.unsplash.com/photo-1542296332-2e4473faf563?w=400");
        flight1.setInternationalTaxPercentage(12.0);
        flight1.setVisaRequired(true);
        flight1.setBaggageAllowanceKg(30);
        flight1.setDestinationCountry("UAE");
        flightRepository.save(flight1);
        
        InternationalFlight flight2 = new InternationalFlight();
        flight2.setFlightNumber("EK606");
        flight2.setAirline("Emirates");
        flight2.setDepartureCity("Mumbai (BOM)");
        flight2.setArrivalCity("Singapore (SIN)");
        flight2.setDepartureTime("2:00 AM");
        flight2.setArrivalTime("9:30 AM");
        flight2.setDuration("5h 30m");
        flight2.setBasePrice(25000.0);
        flight2.setAvailableSeats(25);
        flight2.setImage("https://images.unsplash.com/photo-1569629743817-70d8db6c323b?w=400");
        flight2.setInternationalTaxPercentage(12.0);
        flight2.setVisaRequired(false);
        flight2.setBaggageAllowanceKg(35);
        flight2.setDestinationCountry("Singapore");
        flightRepository.save(flight2);
        
        System.out.println("✅ Created 2 International Flights");
    }
    
    private void createSampleUsers() {
        // Create a sample passenger
        Passenger passenger = new Passenger();
        passenger.setEmail("passenger@test.com");
        passenger.setPassword("password123");
        passenger.setName("John Doe");
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setPhone("9876543210");
        passenger.setLocation("Delhi");
        passenger.setLoyaltyPoints(500);
        userRepository.save(passenger);
        
        // Create a sample admin
        Admin admin = new Admin();
        admin.setEmail("admin@skybook.com");
        admin.setPassword("admin123");
        admin.setName("Admin User");
        admin.setPhone("9999999999");
        admin.setDepartment("Operations");
        admin.setAdminLevel(2);
        admin.setCanManageFlights(true);
        admin.setCanManageUsers(true);
        userRepository.save(admin);
        
        System.out.println("✅ Created Sample Users (Passenger & Admin)");
    }
}