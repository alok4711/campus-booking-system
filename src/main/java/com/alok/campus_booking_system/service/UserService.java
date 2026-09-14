package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.dto.RegisterRequest;
import com.alok.campus_booking_system.enums.Role;
import com.alok.campus_booking_system.enums.UserStatus;
import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;  

@Service 
public class UserService {

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        if (!request.getEmail().toLowerCase().endsWith("@student.annauniv.edu")) {
            throw new IllegalArgumentException("Only college email addresses are allowed");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.STUDENT));
        user.setStatus(UserStatus.UNVERIFIED);

        return userRepository.save(user);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
