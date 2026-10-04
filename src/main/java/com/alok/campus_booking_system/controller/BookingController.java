package com.alok.campus_booking_system.controller;

import com.alok.campus_booking_system.entity.Booking;
import com.alok.campus_booking_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController 
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired 
    private BookingService bookingService;

    @PostMapping 
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping
    public List<Booking> getAllBookings(Authentication authentication) {
        return bookingService.getAllBookings(authentication);
    }

    @PreAuthorize("hasRole('DEAN') or (hasRole('HOD') and @permissionGuard.canManageBooking(authentication, #bookingId))")
    @PutMapping("/{bookingId}/approve")
    public Booking approveBooking(@PathVariable Long bookingId) {
        return bookingService.approveBooking(bookingId);
    }
    
}
