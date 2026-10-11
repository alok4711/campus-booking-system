package com.alok.campus_booking_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendVerificationEmail(String toEmail, String name, String token) {
        String link = frontendUrl + "/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Verify your Campus Booking account");
        message.setText(
            "Hi " + name + ",\n\n"
            + "Welcome to Campus Booking. Please verify your email by opening this link (valid for 24 hours):\n\n"
            + link + "\n\n"
            + "If you did not create this account, you can ignore this email."
        );

        mailSender.send(message);
    }
}