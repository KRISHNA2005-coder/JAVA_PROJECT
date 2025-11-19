package com.skybook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlightBookingApplication.class, args);
        System.out.println("\n=================================");
        System.out.println("SkyBook API is running!");
        System.out.println("API URL: http://localhost:8080");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("=================================\n");
    }
}