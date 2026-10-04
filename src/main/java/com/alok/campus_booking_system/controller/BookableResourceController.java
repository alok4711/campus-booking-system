package com.alok.campus_booking_system.controller;

import com.alok.campus_booking_system.entity.BookableResource;
import com.alok.campus_booking_system.service.BookableResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


import java.util.List;

@RestController
@RequestMapping("/api/bookable-resources")
public class BookableResourceController {

    @Autowired 
    private BookableResourceService bookableResourceService;

    @PreAuthorize("hasRole('DEAN') or (hasRole('HOD') and @permissionGuard.canManageDepartment(authentication, #bookableResource.department.id))")
    @PostMapping 
    public BookableResource createBookableResource(@RequestBody BookableResource bookableResource) {
        return bookableResourceService.createBookableResource(bookableResource);
    }

    @GetMapping
    public List<BookableResource> getAllBookableResources(Authentication authentication) {
        return bookableResourceService.getAllBookableResources(authentication);
    }
    
}
