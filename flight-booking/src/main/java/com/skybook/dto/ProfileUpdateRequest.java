package com.skybook.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String address;
    private String dateOfBirth;
}