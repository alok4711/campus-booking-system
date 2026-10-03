package com.alok.campus_booking_system.controller;

import com.alok.campus_booking_system.dto.LoginRequest;
import com.alok.campus_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return Map.of("token", token);
    }
}