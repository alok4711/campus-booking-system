package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.dto.LoginRequest;
import com.alok.campus_booking_system.dto.RegisterRequest;
import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.entity.VerificationToken;
import com.alok.campus_booking_system.enums.Role;
import com.alok.campus_booking_system.enums.UserStatus;
import com.alok.campus_booking_system.repository.UserRepository;
import com.alok.campus_booking_system.repository.VerificationTokenRepository;
import com.alok.campus_booking_system.security.JwtUtil;
import com.alok.campus_booking_system.dto.UserResponse;
import com.alok.campus_booking_system.entity.Department;
import com.alok.campus_booking_system.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DepartmentRepository departmentRepository;

    public UserResponse register(RegisterRequest request) {

        if (!request.getEmail().toLowerCase().endsWith("@student.annauniv.edu")
                && !request.getEmail().toLowerCase().endsWith("@annauniv.edu")
                && !request.getEmail().toLowerCase().endsWith("@gmail.com")) {
            throw new IllegalArgumentException("Only college email addresses are allowed");
        }

        if (request.getDepartmentId() == null) {
            throw new IllegalArgumentException("Please select your department");
        }
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.STUDENT));
        user.setStatus(UserStatus.UNVERIFIED);
        user.setDepartment(department);

        User savedUser = userRepository.save(user);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryTime(LocalDateTime.now().plusHours(24));

        verificationTokenRepository.save(verificationToken);

        try {
            emailService.sendVerificationEmail(
                    savedUser.getEmail(), savedUser.getName(), verificationToken.getToken());
        } catch (Exception e) {
            log.error("Could not send verification email to {}: {}", savedUser.getEmail(), e.getMessage());
        }

        return toResponse(savedUser);
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active yet");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getName(), user.getRoles());
    }

    public String verifyEmail(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        if (verificationToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);

        return "Email verified successfully";
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    private UserResponse toResponse(User user) {
        UserResponse r = new UserResponse();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setRoles(user.getRoles());
        r.setStatus(user.getStatus());
        r.setDepartmentName(user.getDepartment() != null ? user.getDepartment().getName() : null);
        return r;
    }
}