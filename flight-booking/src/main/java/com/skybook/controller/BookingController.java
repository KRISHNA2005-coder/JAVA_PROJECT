package com.skybook.controller;

import com.skybook.dto.BookingRequest;
import com.skybook.model.Booking;
import com.skybook.service.BookingService;
import com.skybook.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * BookingController - REST API endpoints for bookings
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class BookingController {
    
    private final BookingService bookingService;
    private final PdfService pdfService;
    
    /**
     * POST /api/bookings
     * Create a new booking
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(@Valid @RequestBody BookingRequest request) {
        Map<String, Object> response = bookingService.createBooking(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/bookings
     * Get user bookings by email
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getUserBookings(@RequestParam String email) {
        return ResponseEntity.ok(bookingService.getUserBookings(email));
    }
    
    /**
     * GET /api/bookings/{id}
     * Get booking by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/bookings/{id}/ticket
     * Download ticket as PDF
     */
    @GetMapping("/{id}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingEntity(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            
            byte[] pdfBytes = pdfService.generateTicketPdf(booking);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ticket-" + booking.getBookingReference() + ".pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /api/bookings/{id}
     * Cancel a booking
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> cancelBooking(@PathVariable Long id) {
        Map<String, Object> response = bookingService.cancelBooking(id);
        return ResponseEntity.ok(response);
    }
}