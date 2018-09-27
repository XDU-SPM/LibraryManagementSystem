package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer>
{
    Category findByName(String name);
}
