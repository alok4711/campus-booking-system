package com.alok.campus_booking_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String name;

    private String email;

    private String password;
}