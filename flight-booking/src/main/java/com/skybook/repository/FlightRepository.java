package com.skybook.repository;

import com.skybook.dto.FlightDTO;
import com.skybook.model.Flight;
import com.skybook.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FlightRepository {
    
    private final FileStorageService fileStorageService;
    private static final String FLIGHTS_FILE = "flights";
    
    public Flight save(Flight flight) {
        List<FlightDTO> dtos = fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class);
        
        if (flight.getId() == null) {
            flight.setId(generateId(dtos));
        }
        
        FlightDTO newDto = FlightDTO.fromFlight(flight);
        
        // Remove old version if updating
        dtos = dtos.stream()
                .filter(dto -> !dto.getId().equals(flight.getId()))
                .collect(Collectors.toList());
        
        dtos.add(newDto);
        fileStorageService.save(FLIGHTS_FILE, dtos);
        return flight;
    }
    
    public Optional<Flight> findById(Long id) {
        return fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .filter(dto -> dto.getId().equals(id))
                .map(FlightDTO::toFlight)
                .findFirst();
    }
    
    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .map(FlightDTO::toFlight)
                .filter(f -> f.getFlightNumber().equals(flightNumber))
                .findFirst();
    }
    
    public List<Flight> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity) {
        return fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .map(FlightDTO::toFlight)
                .filter(f -> f.getDepartureCity().equalsIgnoreCase(departureCity) 
                          && f.getArrivalCity().equalsIgnoreCase(arrivalCity))
                .collect(Collectors.toList());
    }
    
    public List<Flight> findByAirline(String airline) {
        return fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .map(FlightDTO::toFlight)
                .filter(f -> f.getAirline().equalsIgnoreCase(airline))
                .collect(Collectors.toList());
    }
    
    public List<Flight> findAvailableFlights() {
        return fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .map(FlightDTO::toFlight)
                .filter(Flight::isAvailable)
                .collect(Collectors.toList());
    }
    
    public List<Flight> findFlightsByLocation(String departure, String arrival) {
        return fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .map(FlightDTO::toFlight)
                .filter(f -> f.getDepartureCity().equalsIgnoreCase(departure) 
                          || f.getArrivalCity().equalsIgnoreCase(arrival))
                .collect(Collectors.toList());
    }
    
    public List<Flight> findAll() {
        return fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .map(FlightDTO::toFlight)
                .collect(Collectors.toList());
    }
    
    public void delete(Flight flight) {
        deleteById(flight.getId());
    }
    
    public void deleteById(Long id) {
        List<FlightDTO> dtos = fileStorageService.loadList(FLIGHTS_FILE, FlightDTO.class).stream()
                .filter(dto -> !dto.getId().equals(id))
                .collect(Collectors.toList());
        fileStorageService.save(FLIGHTS_FILE, dtos);
    }
    
    private Long generateId(List<FlightDTO> dtos) {
        return dtos.stream()
                .mapToLong(FlightDTO::getId)
                .max()
                .orElse(0L) + 1;
    }
}