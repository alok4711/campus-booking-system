package com.alok.campus_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alok.campus_booking_system.entity.BookableResource;

public interface BookableResourceRepository extends JpaRepository<BookableResource, Long>{
    
}
