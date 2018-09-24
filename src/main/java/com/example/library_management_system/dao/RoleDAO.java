package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Integer>
{
    Role findByName(String name);
}
