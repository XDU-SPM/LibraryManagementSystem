package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ReviewDAO extends JpaRepository<Review,String>
{
    Set<Review> findAllByBook(Book book);


}
