package com.skybook.dto;

import com.skybook.model.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;
    private String bookingReference;
    private Long passengerId;
    private Long flightId;
    private LocalDateTime bookingDate;
    private String status;
    private Double totalAmount;
    private String paymentMethod;
    private String transactionId;
    private String passengerFirstName;
    private String passengerLastName;
    private String passengerEmail;
    private String passengerPhone;
    
    public static BookingDTO fromBooking(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setBookingReference(booking.getBookingReference());
        dto.setPassengerId(booking.getPassenger() != null ? booking.getPassenger().getId() : null);
        dto.setFlightId(booking.getFlight() != null ? booking.getFlight().getId() : null);
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setPaymentMethod(booking.getPaymentMethod());
        dto.setTransactionId(booking.getTransactionId());
        dto.setPassengerFirstName(booking.getPassengerFirstName());
        dto.setPassengerLastName(booking.getPassengerLastName());
        dto.setPassengerEmail(booking.getPassengerEmail());
        dto.setPassengerPhone(booking.getPassengerPhone());
        return dto;
    }
}