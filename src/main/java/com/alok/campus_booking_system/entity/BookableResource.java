package com.alok.campus_booking_system.entity;

import com.alok.campus_booking_system.enums.ResourceStatus;
import com.alok.campus_booking_system.enums.ResourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class BookableResource {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceName;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status;

    @ManyToOne 
    @JoinColumn(name = "department_id")
    private Department department;

    private boolean requiresPayment;
}
