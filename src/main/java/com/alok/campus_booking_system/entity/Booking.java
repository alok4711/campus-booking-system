package com.alok.campus_booking_system.entity;

import java.time.LocalDateTime;
import com.alok.campus_booking_system.enums.BookingStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class Booking {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingPurpose;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer attendeesCount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne 
    @JoinColumn (name = "user_id")
    private User bookedBy;

    @ManyToOne 
    @JoinColumn (name = "resource_id")
    private BookableResource resource;
}
