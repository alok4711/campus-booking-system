package com.alok.campus_booking_system.entity;

import com.alok.campus_booking_system.enums.Role;
import com.alok.campus_booking_system.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}