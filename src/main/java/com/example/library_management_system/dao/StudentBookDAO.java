package com.example.library_management_system.dao;

import com.example.library_management_system.bean.StudentBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentBookDAO extends JpaRepository<StudentBook, Integer>
{
}
