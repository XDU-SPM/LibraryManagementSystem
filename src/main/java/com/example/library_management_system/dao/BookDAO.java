package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDAO extends JpaRepository<Book, String>
{

}
