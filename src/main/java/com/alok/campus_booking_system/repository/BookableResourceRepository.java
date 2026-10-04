package com.alok.campus_booking_system.repository;

import com.alok.campus_booking_system.entity.BookableResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.List;

public interface BookableResourceRepository extends JpaRepository<BookableResource, Long>{
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BookableResource> findById(Long id);

    List<BookableResource> findByDepartmentId(Long departmentId);

}
