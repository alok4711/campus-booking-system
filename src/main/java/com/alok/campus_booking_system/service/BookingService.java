package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.entity.Booking;
import com.alok.campus_booking_system.enums.BookingStatus;
import com.alok.campus_booking_system.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                booking.getResource(), booking.getStartTime(), booking.getEndTime());

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("This resource is already booked for the selected time slot");
        }

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
