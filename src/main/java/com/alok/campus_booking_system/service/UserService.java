package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.dto.RegisterRequest;
import com.alok.campus_booking_system.dto.LoginRequest;
import com.alok.campus_booking_system.security.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active yet");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

    public User register(RegisterRequest request) {

        if (!request.getEmail().toLowerCase().endsWith("@student.annauniv.edu") && !request.getEmail().toLowerCase().endsWith("@annauniv.edu")) {
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
