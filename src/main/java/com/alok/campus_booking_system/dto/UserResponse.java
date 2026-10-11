package com.alok.campus_booking_system.dto;

import com.alok.campus_booking_system.enums.Role;
import com.alok.campus_booking_system.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
    private UserStatus status;
    private String departmentName;
}