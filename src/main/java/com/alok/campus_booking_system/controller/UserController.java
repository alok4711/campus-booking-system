package com.alok.campus_booking_system.controller;

import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.service.UserService;
import com.alok.campus_booking_system.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping ("/api/users")
public class UserController {

    @Autowired 
    private UserService userService;

    @PostMapping 
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }
    
}
