package com.skybook.service;

import com.skybook.model.Flight;
import com.skybook.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FlightService - Flight business logic
 * Demonstrates polymorphism - works with abstract Flight class
 */
@Service
@RequiredArgsConstructor
public class FlightService {
    
    private final FlightRepository flightRepository;
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAvailableFlights() {
        List<Flight> flights = flightRepository.findAvailableFlights();
        return flights.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<Map<String, Object>> getFlightById(Long id) {
        return flightRepository.findById(id)
            .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> searchFlights(String from, String to) {
        List<Flight> flights;
        
        if (from != null && to != null) {
            flights = flightRepository.findByDepartureCityAndArrivalCity(from, to);
        } else if (from != null || to != null) {
            String location = from != null ? from : to;
            flights = flightRepository.findFlightsByLocation(location, location);
        } else {
            flights = flightRepository.findAll();
        }
        
        return flights.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    private Map<String, Object> convertToResponse(Flight flight) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", flight.getId());
        response.put("flightNumber", flight.getFlightNumber());
        response.put("airline", flight.getAirline());
        response.put("from", flight.getDepartureCity());
        response.put("to", flight.getArrivalCity());
        response.put("departure", flight.getDepartureTime());
        response.put("arrival", flight.getArrivalTime());
        response.put("duration", flight.getDuration());
        response.put("price", "₹" + flight.getBasePrice().intValue());
        response.put("finalPrice", flight.calculateFinalPrice()); // Polymorphic method
        response.put("availableSeats", flight.getAvailableSeats());
        response.put("image", flight.getImage());
        response.put("flightType", flight.getFlightType()); // Polymorphic method
        response.put("requiresVisa", flight.requiresVisa()); // Polymorphic method
        response.put("flightInfo", flight.getFlightInfo()); // Polymorphic method
        return response;
    }
}