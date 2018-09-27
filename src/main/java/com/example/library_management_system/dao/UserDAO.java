package com.example.library_management_system.dao;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer>
{
    User findByUsername(String username);
    User findById(int id);
}
