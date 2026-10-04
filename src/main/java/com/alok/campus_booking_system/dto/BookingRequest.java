package com.alok.campus_booking_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequest {

    private String bookingPurpose;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer attendeesCount;

    private Long resourceId;
}