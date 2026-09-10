package com.alok.campus_booking_system.repository;

import com.alok.campus_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    
}
