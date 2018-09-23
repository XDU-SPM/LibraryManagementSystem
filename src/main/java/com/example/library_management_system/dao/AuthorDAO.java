package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorDAO extends JpaRepository<Author, Integer>
{
}
