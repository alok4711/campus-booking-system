package com.alok.campus_booking_system.service;

import com.alok.campus_booking_system.entity.BookableResource;
import com.alok.campus_booking_system.repository.BookableResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class BookableResourceService {

    @Autowired 
    private BookableResourceRepository bookableResourceRepository;

    public BookableResource createBookableResource(BookableResource bookableResource) {
        return bookableResourceRepository.save(bookableResource);
    }

    public List<BookableResource> getAllBookableResources() {
        return bookableResourceRepository.findAll();
    }
    
}
