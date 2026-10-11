package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.entity.BookableResource;
import com.alok.campus_booking_system.entity.Booking;
import com.alok.campus_booking_system.enums.BookingStatus;
import com.alok.campus_booking_system.repository.BookingRepository;
import com.alok.campus_booking_system.repository.BookableResourceRepository;
import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.repository.UserRepository;
import com.alok.campus_booking_system.enums.Role;
import com.alok.campus_booking_system.dto.BookingRequest;
import com.alok.campus_booking_system.enums.ResourceStatus;
import com.alok.campus_booking_system.dto.BookingResponse;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookableResourceRepository bookableResourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, Authentication authentication) {

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("Start and end time are required");
        }
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start time cannot be in the past");
        }
        if (request.getAttendeesCount() == null || request.getAttendeesCount() < 1) {
            throw new IllegalArgumentException("Attendees must be at least 1");
        }

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BookableResource lockedResource = bookableResourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        if (lockedResource.getStatus() != ResourceStatus.ACTIVE) {
            throw new IllegalArgumentException("This resource is not available for booking");
        }

        Integer capacity = lockedResource.getCapacity();
        if (capacity != null && request.getAttendeesCount() > capacity) {
            throw new IllegalArgumentException(
                    lockedResource.getResourceName() + " holds only " + capacity + " people");
        }

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                lockedResource, request.getStartTime(), request.getEndTime());

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("This resource is already booked for the selected time slot");
        }

        Booking booking = new Booking();
        booking.setBookingPurpose(request.getBookingPurpose());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setAttendeesCount(request.getAttendeesCount());
        booking.setStatus(BookingStatus.PENDING);
        booking.setBookedBy(currentUser);
        booking.setResource(lockedResource);

        return toResponse(bookingRepository.save(booking), true, true);
    }

    public BookingResponse rejectBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(BookingStatus.REJECTED);
        return toResponse(bookingRepository.save(booking), true, false);
    }

    public BookingResponse approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(BookingStatus.APPROVED);
        return toResponse(bookingRepository.save(booking), true, false);
    }
    
    public List<BookingResponse> getAllBookings(Authentication authentication) {

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isDean = currentUser.getRoles().contains(Role.DEAN);
        boolean isReviewer = isDean || currentUser.getRoles().contains(Role.HOD);

        List<Booking> source = (isDean || currentUser.getDepartment() == null)
                ? bookingRepository.findAll()
                : bookingRepository.findByResourceDepartmentId(currentUser.getDepartment().getId());

        List<BookingResponse> result = new ArrayList<>();
        for (Booking b : source) {
            boolean mine = b.getBookedBy() != null && b.getBookedBy().getId().equals(currentUser.getId());
            boolean activeSlot = b.getStatus() == BookingStatus.PENDING || b.getStatus() == BookingStatus.APPROVED;

            if (!isReviewer && !mine && !activeSlot) {
                continue;
            }
            result.add(toResponse(b, isReviewer || mine, mine));
        }
        return result;
    }

    private BookingResponse toResponse(Booking b, boolean showDetails, boolean mine) {
        BookingResponse r = new BookingResponse();
        r.setId(b.getId());
        r.setStartTime(b.getStartTime());
        r.setEndTime(b.getEndTime());
        r.setStatus(b.getStatus());
        r.setResourceId(b.getResource().getId());
        r.setResourceName(b.getResource().getResourceName());
        r.setMine(mine);

        if (showDetails) {
            r.setBookingPurpose(b.getBookingPurpose());
            r.setAttendeesCount(b.getAttendeesCount());
            r.setBookedByName(b.getBookedBy().getName());
            r.setBookedByEmail(b.getBookedBy().getEmail());
        } else {
            r.setBookingPurpose("Booked");
        }
        return r;
    }
    
}
