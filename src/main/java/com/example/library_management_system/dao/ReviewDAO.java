package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDAO extends JpaRepository<Review,String>
{


    Review findById(int id);

    Page<Review> findAllByBook(Book book, Pageable pageable);


}
