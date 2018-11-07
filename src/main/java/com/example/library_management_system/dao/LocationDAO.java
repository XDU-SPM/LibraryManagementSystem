package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDAO extends JpaRepository<Location, Integer>
{
    Location findByName(String name);
}
