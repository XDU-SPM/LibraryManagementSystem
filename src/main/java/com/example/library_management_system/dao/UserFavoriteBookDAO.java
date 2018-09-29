package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserFavoriteBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteBookDAO extends JpaRepository<UserFavoriteBook, Integer>
{

    Page<UserFavoriteBook> findAllByUser(User reader, Pageable pageable);

    UserFavoriteBook findByUserAndBook(User reader, Book book);



}
