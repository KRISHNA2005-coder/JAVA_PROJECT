package com.skybook.dto;

import com.skybook.model.*;
import lombok.Data;

@Data
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private String airline;
    private String departureCity;
    private String arrivalCity;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private Double basePrice;
    private Integer availableSeats;
    private String image;
    
    // Flight type specific fields
    private String type; // "DOMESTIC" or "INTERNATIONAL"
    private Double stateTaxPercentage;
    private Boolean includesMeal;
    private Double internationalTaxPercentage;
    private Boolean visaRequired;
    private Integer baggageAllowanceKg;
    private String destinationCountry;
    
    public static FlightDTO fromFlight(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setAirline(flight.getAirline());
        dto.setDepartureCity(flight.getDepartureCity());
        dto.setArrivalCity(flight.getArrivalCity());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setDuration(flight.getDuration());
        dto.setBasePrice(flight.getBasePrice());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setImage(flight.getImage());
        
        if (flight instanceof DomesticFlight) {
            DomesticFlight df = (DomesticFlight) flight;
            dto.setType("DOMESTIC");
            dto.setStateTaxPercentage(df.getStateTaxPercentage());
            dto.setIncludesMeal(df.getIncludesMeal());
        } else if (flight instanceof InternationalFlight) {
            InternationalFlight inf = (InternationalFlight) flight;
            dto.setType("INTERNATIONAL");
            dto.setInternationalTaxPercentage(inf.getInternationalTaxPercentage());
            dto.setVisaRequired(inf.getVisaRequired());
            dto.setBaggageAllowanceKg(inf.getBaggageAllowanceKg());
            dto.setDestinationCountry(inf.getDestinationCountry());
        }
        
        return dto;
    }
    
    public Flight toFlight() {
        if ("DOMESTIC".equals(type)) {
            DomesticFlight flight = new DomesticFlight();
            flight.setId(id);
            flight.setFlightNumber(flightNumber);
            flight.setAirline(airline);
            flight.setDepartureCity(departureCity);
            flight.setArrivalCity(arrivalCity);
            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);
            flight.setDuration(duration);
            flight.setBasePrice(basePrice);
            flight.setAvailableSeats(availableSeats);
            flight.setImage(image);
            flight.setStateTaxPercentage(stateTaxPercentage);
            flight.setIncludesMeal(includesMeal);
            return flight;
        } else {
            InternationalFlight flight = new InternationalFlight();
            flight.setId(id);
            flight.setFlightNumber(flightNumber);
            flight.setAirline(airline);
            flight.setDepartureCity(departureCity);
            flight.setArrivalCity(arrivalCity);
            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);
            flight.setDuration(duration);
            flight.setBasePrice(basePrice);
            flight.setAvailableSeats(availableSeats);
            flight.setImage(image);
            flight.setInternationalTaxPercentage(internationalTaxPercentage);
            flight.setVisaRequired(visaRequired);
            flight.setBaggageAllowanceKg(baggageAllowanceKg);
            flight.setDestinationCountry(destinationCountry);
            return flight;
        }
    }
}