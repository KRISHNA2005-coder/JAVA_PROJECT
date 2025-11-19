package com.skybook.repository;

import com.skybook.dto.BookingDTO;
import com.skybook.model.Booking;
import com.skybook.model.Passenger;
import com.skybook.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingRepository {
    
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private static final String BOOKINGS_FILE = "bookings";
    
    public Booking save(Booking booking) {
        List<BookingDTO> dtos = fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class);
        
        if (booking.getId() == null) {
            booking.setId(generateId(dtos));
        }
        
        BookingDTO newDto = BookingDTO.fromBooking(booking);
        
        // Remove old version if updating
        dtos = dtos.stream()
                .filter(dto -> !dto.getId().equals(booking.getId()))
                .collect(Collectors.toList());
        
        dtos.add(newDto);
        fileStorageService.save(BOOKINGS_FILE, dtos);
        return booking;
    }
    
    private Booking dtoToBooking(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setBookingReference(dto.getBookingReference());
        booking.setBookingDate(dto.getBookingDate());
        booking.setStatus(dto.getStatus());
        booking.setTotalAmount(dto.getTotalAmount());
        booking.setPaymentMethod(dto.getPaymentMethod());
        booking.setTransactionId(dto.getTransactionId());
        booking.setPassengerFirstName(dto.getPassengerFirstName());
        booking.setPassengerLastName(dto.getPassengerLastName());
        booking.setPassengerEmail(dto.getPassengerEmail());
        booking.setPassengerPhone(dto.getPassengerPhone());
        
        // Load relationships with null checks
        if (dto.getPassengerId() != null) {
            userRepository.findById(dto.getPassengerId()).ifPresent(user -> {
                if (user instanceof Passenger) {
                    booking.setPassenger((Passenger) user);
                }
            });
        }
        
        if (dto.getFlightId() != null) {
            flightRepository.findById(dto.getFlightId()).ifPresent(booking::setFlight);
        }
        
        return booking;
    }
    
    public Optional<Booking> findById(Long id) {
        return fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .filter(dto -> dto.getId().equals(id))
                .map(this::dtoToBooking)
                .findFirst();
    }
    
    public Optional<Booking> findByBookingReference(String bookingReference) {
        return fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .filter(dto -> dto.getBookingReference().equals(bookingReference))
                .map(this::dtoToBooking)
                .findFirst();
    }
    
    public List<Booking> findByPassenger(Passenger passenger) {
        return fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .filter(dto -> dto.getPassengerId() != null && 
                           dto.getPassengerId().equals(passenger.getId()))
                .map(this::dtoToBooking)
                .collect(Collectors.toList());
    }
    
    public List<Booking> findByPassengerId(Long passengerId) {
        return fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .filter(dto -> dto.getPassengerId() != null && 
                           dto.getPassengerId().equals(passengerId))
                .map(this::dtoToBooking)
                .collect(Collectors.toList());
    }
    
    public List<Booking> findByStatus(String status) {
        return fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .filter(dto -> dto.getStatus().equalsIgnoreCase(status))
                .map(this::dtoToBooking)
                .collect(Collectors.toList());
    }
    
    public List<Booking> findByPassengerEmail(String email) {
        return fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .filter(dto -> dto.getPassengerEmail().equals(email))
                .map(this::dtoToBooking)
                .collect(Collectors.toList());
    }
    
    public List<Booking> findAll() {
        return fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .map(this::dtoToBooking)
                .collect(Collectors.toList());
    }
    
    public void delete(Booking booking) {
        deleteById(booking.getId());
    }
    
    public void deleteById(Long id) {
        List<BookingDTO> dtos = fileStorageService.loadList(BOOKINGS_FILE, BookingDTO.class).stream()
                .filter(dto -> !dto.getId().equals(id))
                .collect(Collectors.toList());
        fileStorageService.save(BOOKINGS_FILE, dtos);
    }
    
    private Long generateId(List<BookingDTO> dtos) {
        return dtos.stream()
                .mapToLong(BookingDTO::getId)
                .max()
                .orElse(0L) + 1;
    }
}