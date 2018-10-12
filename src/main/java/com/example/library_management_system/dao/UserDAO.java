package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer>
{
    User findByUsername(String username);

    User findById(int id);

    Page<User> findByRolesContaining(Role role, Pageable pageable);

    List<User> findByRolesContaining(Role role);

    Page<User> findAll(Pageable pageable);
}