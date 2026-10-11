package com.alok.campus_booking_system.dto;

import com.alok.campus_booking_system.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponse {

    private Long id;
    private String bookingPurpose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer attendeesCount;
    private BookingStatus status;
    private Long resourceId;
    private String resourceName;
    private String bookedByName;
    private String bookedByEmail;
    private boolean mine;
    private String classSection;
}