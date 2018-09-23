package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDAO extends JpaRepository<Admin, Integer>
{
    Admin findByUsername(String username);
}
