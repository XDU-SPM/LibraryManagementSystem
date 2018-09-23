package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDAO extends JpaRepository<Student, Integer>
{
    Student findByUsername(String username);
}
