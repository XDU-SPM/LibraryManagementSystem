package com.example.library_management_system.dao;

import com.example.library_management_system.bean.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookDAO extends JpaRepository<UserBook, Integer>
{
}
