package com.alok.campus_booking_system.controller;

import com.alok.campus_booking_system.dto.UserResponse;
import com.alok.campus_booking_system.service.UserService;
import com.alok.campus_booking_system.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController 
@RequestMapping ("/api/users")
public class UserController {

    @Autowired 
    private UserService userService;

    @PreAuthorize("hasRole('DEAN')")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        return userService.verifyEmail(token);
    }
    
}
