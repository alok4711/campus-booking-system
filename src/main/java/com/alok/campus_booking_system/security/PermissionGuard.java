package com.alok.campus_booking_system.security;

import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionGuard")
public class PermissionGuard {

    @Autowired
    private UserRepository userRepository;

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
}