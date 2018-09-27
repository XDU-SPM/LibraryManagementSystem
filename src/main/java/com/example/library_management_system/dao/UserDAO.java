package com.example.library_management_system.dao;

import com.example.library_management_system.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer>
{
    User findByUsername(String username);
}
