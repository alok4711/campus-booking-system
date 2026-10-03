package com.alok.campus_booking_system.controller;

import com.alok.campus_booking_system.entity.BookableResource;
import com.alok.campus_booking_system.service.BookableResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;

@RestController
@RequestMapping("/api/bookable-resources")
public class BookableResourceController {

    @Autowired 
    private BookableResourceService bookableResourceService;

    @PreAuthorize("hasAnyRole('HOD', 'DEAN')")
    @PostMapping 
    public BookableResource createBookableResource(@RequestBody BookableResource bookableResource) {
        return bookableResourceService.createBookableResource(bookableResource);
    }

    @GetMapping 
    public List<BookableResource> getAllBookableResources() {
        return bookableResourceService.getAllBookableResources();
    }
    
}
