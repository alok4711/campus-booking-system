package com.alok.campus_booking_system.repository;

import com.alok.campus_booking_system.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    
}
