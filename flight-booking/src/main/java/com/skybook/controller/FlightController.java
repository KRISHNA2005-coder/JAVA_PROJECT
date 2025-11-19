package com.skybook.controller;

import com.skybook.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * FlightController - REST API endpoints for flights
 */
@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class FlightController {
    
    private final FlightService flightService;
    
    /**
     * GET /api/flights
     * Get all flights or search flights
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getFlights(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        
        if (from != null || to != null) {
            return ResponseEntity.ok(flightService.searchFlights(from, to));
        }
        return ResponseEntity.ok(flightService.getAvailableFlights());
    }
    
    /**
     * GET /api/flights/{id}
     * Get flight by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/flights/search
     * Search flights with parameters
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchFlights(
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String arrival) {
        
        return ResponseEntity.ok(flightService.searchFlights(departure, arrival));
    }
}