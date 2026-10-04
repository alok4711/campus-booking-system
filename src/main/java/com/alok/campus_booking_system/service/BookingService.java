package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.entity.BookableResource;
import com.alok.campus_booking_system.entity.Booking;
import com.alok.campus_booking_system.enums.BookingStatus;
import com.alok.campus_booking_system.repository.BookingRepository;
import com.alok.campus_booking_system.repository.BookableResourceRepository;
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

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(BookingStatus.APPROVED);
        return bookingRepository.save(booking);
    }
    
}
