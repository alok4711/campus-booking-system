package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.entity.BookableResource;
import com.alok.campus_booking_system.entity.Booking;
import com.alok.campus_booking_system.enums.BookingStatus;
import com.alok.campus_booking_system.repository.BookingRepository;
import com.alok.campus_booking_system.repository.BookableResourceRepository;
import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.repository.UserRepository;
import com.alok.campus_booking_system.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookableResourceRepository bookableResourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Booking createBooking(Booking booking) {

        BookableResource lockedResource = bookableResourceRepository.findById(booking.getResource().getId())
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                lockedResource, booking.getStartTime(), booking.getEndTime());

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("This resource is already booked for the selected time slot");
        }

        booking.setResource(lockedResource);
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings(Authentication authentication) {

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isDean = currentUser.getRoles().contains(Role.DEAN);

        if (isDean || currentUser.getDepartment() == null) {
            return bookingRepository.findAll();
        }

        return bookingRepository.findByResourceDepartmentId(currentUser.getDepartment().getId());
    }

    public Booking approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(BookingStatus.APPROVED);
        return bookingRepository.save(booking);
    }
    
}
