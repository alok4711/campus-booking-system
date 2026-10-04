package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.entity.BookableResource;
import com.alok.campus_booking_system.repository.BookableResourceRepository;
import com.alok.campus_booking_system.entity.User;
import com.alok.campus_booking_system.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alok.campus_booking_system.enums.Role;

import java.util.List;

@Service 
public class BookableResourceService {

    @Autowired 
    private BookableResourceRepository bookableResourceRepository;

    @Autowired
    private UserRepository userRepository;

    public BookableResource createBookableResource(BookableResource bookableResource) {
        return bookableResourceRepository.save(bookableResource);
    }

    public List<BookableResource> getAllBookableResources(Authentication authentication) {

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isDean = currentUser.getRoles().contains(Role.DEAN);

        if (isDean || currentUser.getDepartment() == null) {
            return bookableResourceRepository.findAll();
        }

        return bookableResourceRepository.findByDepartmentId(currentUser.getDepartment().getId());
    }
    
}
