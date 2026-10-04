package com.alok.campus_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alok.campus_booking_system.entity.Booking;
import com.alok.campus_booking_system.entity.BookableResource;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    
    @Query("SELECT b FROM Booking b WHERE b.resource = :resource " +
        "AND b.status <> 'REJECTED' AND b.status <> 'CANCELLED' " +
        "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findConflictingBookings(
            @Param("resource") BookableResource resource,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<Booking> findByResourceDepartmentId(Long departmentId);
    
}
