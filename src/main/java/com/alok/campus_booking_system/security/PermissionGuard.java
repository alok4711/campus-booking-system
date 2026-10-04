package com.alok.campus_booking_system.security;

import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.repository.UserRepository;
import com.alok.campus_booking_system.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionGuard")
public class PermissionGuard {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public boolean canManageDepartment(Authentication authentication, Long departmentId) {

        if (departmentId == null) {
            return false;
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || user.getDepartment() == null) {
            return false;
        }

        return user.getDepartment().getId().equals(departmentId);
    }

    public boolean canManageBooking(Authentication authentication, Long bookingId) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || user.getDepartment() == null) {
            return false;
        }

        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getResource().getDepartment().getId().equals(user.getDepartment().getId()))
                .orElse(false);
    }
}