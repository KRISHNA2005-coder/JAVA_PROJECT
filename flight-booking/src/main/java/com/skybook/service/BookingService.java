package com.skybook.service;

import com.skybook.dto.BookingRequest;
import com.skybook.model.Booking;
import com.skybook.model.Flight;
import com.skybook.model.Passenger;
import com.skybook.model.User;
import com.skybook.model.payment.*;
import com.skybook.repository.BookingRepository;
import com.skybook.repository.FlightRepository;
import com.skybook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BookingService - Demonstrates polymorphism with Payment interface
 */
@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public Map<String, Object> createBooking(BookingRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        // Get flight
        Optional<Flight> flightOpt = flightRepository.findById(request.getFlightId());
        if (flightOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Flight not found");
            return response;
        }
        
        Flight flight = flightOpt.get();
        
        // Check availability
        if (!flight.isAvailable()) {
            response.put("success", false);
            response.put("message", "No seats available");
            return response;
        }
        
        // Get or create passenger
        Passenger passenger = getOrCreatePassenger(request);
        
        // Calculate total amount
        Double finalPrice = flight.calculateFinalPrice(); // Polymorphic call
        Double taxes = 500.0;
        Double totalAmount = finalPrice + taxes;
        
        // Process payment using polymorphism
        Payment payment = createPaymentMethod(request);
        boolean paymentSuccess = payment.processPayment(totalAmount);
        
        if (!paymentSuccess) {
            response.put("success", false);
            response.put("message", "Payment failed");
            return response;
        }
        
        // Create booking
        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setBookingDate(LocalDateTime.now()); // SET BOOKING DATE
        booking.setPassenger(passenger);
        booking.setFlight(flight);
        booking.setTotalAmount(totalAmount);
        booking.setPaymentMethod(payment.getPaymentMethod());
        booking.setTransactionId(payment.getTransactionId());
        booking.setPassengerFirstName(request.getFirstName());
        booking.setPassengerLastName(request.getLastName());
        booking.setPassengerEmail(request.getEmail());
        booking.setPassengerPhone(request.getPhone());
        booking.setStatus("CONFIRMED");
        
        // Book seat
        flight.bookSeat();
        flightRepository.save(flight);
        
        // Add loyalty points
        passenger.addLoyaltyPoints(totalAmount.intValue() / 100);
        userRepository.save(passenger);
        
        Booking savedBooking = bookingRepository.save(booking);
        
        response.put("success", true);
        response.put("message", "Booking confirmed");
        response.put("booking", convertToResponse(savedBooking));
        
        return response;
    }
    
    /**
     * Demonstrates POLYMORPHISM - returns different payment implementations
     */
    private Payment createPaymentMethod(BookingRequest request) {
        String paymentType = request.getPaymentType();
        
        if ("CREDIT_CARD".equals(paymentType)) {
            return new CreditCardPayment(
                request.getCardNumber(),
                request.getFirstName() + " " + request.getLastName(),
                request.getCardExpiry(),
                request.getCardCvv()
            );
        } else if ("DEBIT_CARD".equals(paymentType)) {
            return new DebitCardPayment(
                request.getCardNumber(),
                request.getFirstName() + " " + request.getLastName(),
                request.getCardExpiry(),
                request.getCardCvv(),
                "SBI Bank"
            );
        } else {
            // Default to UPI
            return new UPIPayment(
                request.getEmail() + "@upi",
                request.getPhone()
            );
        }
    }
    
    private Passenger getOrCreatePassenger(BookingRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isPresent() && userOpt.get() instanceof Passenger) {
            return (Passenger) userOpt.get();
        }
        
        // Create new passenger
        Passenger passenger = new Passenger();
        passenger.setEmail(request.getEmail());
        passenger.setPassword("temp123"); // Temporary password
        passenger.setName(request.getFirstName() + " " + request.getLastName());
        passenger.setFirstName(request.getFirstName());
        passenger.setLastName(request.getLastName());
        passenger.setPhone(request.getPhone());
        
        return (Passenger) userRepository.save(passenger);
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserBookings(String email) {
        List<Booking> bookings = bookingRepository.findByPassengerEmail(email);
        return bookings.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> getBookingById(Long id) {
        return bookingRepository.findById(id)
            .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingEntity(Long id) {
        return bookingRepository.findById(id);
    }
    
    @Transactional
    public Map<String, Object> cancelBooking(Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Booking not found");
            return response;
        }
        
        Booking booking = bookingOpt.get();
        
        if (!booking.canBeCancelled()) {
            response.put("success", false);
            response.put("message", "Booking cannot be cancelled");
            return response;
        }
        
        booking.cancel();
        
        // Return seat to flight
        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);
        
        bookingRepository.save(booking);
        
        response.put("success", true);
        response.put("message", "Booking cancelled successfully");
        
        return response;
    }
    
    private Map<String, Object> convertToResponse(Booking booking) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", booking.getId());
        response.put("bookingRef", booking.getBookingReference());
        response.put("status", booking.getStatus());
        response.put("from", booking.getFlight().getDepartureCity());
        response.put("to", booking.getFlight().getArrivalCity());
        response.put("departure", booking.getFlight().getDepartureTime());
        response.put("arrival", booking.getFlight().getArrivalTime());
        response.put("airline", booking.getFlight().getAirline());
        response.put("totalAmount", booking.getTotalAmount());
        response.put("paymentMethod", booking.getPaymentMethod());
        response.put("transactionId", booking.getTransactionId());
        
        Map<String, String> passengerInfo = new HashMap<>();
        passengerInfo.put("firstName", booking.getPassengerFirstName());
        passengerInfo.put("lastName", booking.getPassengerLastName());
        passengerInfo.put("email", booking.getPassengerEmail());
        passengerInfo.put("phone", booking.getPassengerPhone());
        response.put("passenger", passengerInfo);
        
        return response;
    }
    
    private String generateBookingReference() {
        return "BK" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}