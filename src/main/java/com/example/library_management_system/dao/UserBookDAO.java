package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookDAO extends JpaRepository<UserBook, Integer>
{
    UserBook findByUserAndBook(User user, Book book);

    Page<UserBook> findAllByUserAndStatus(User user, int status, Pageable pageable);

    Page<UserBook> findAllByStatusBetween(int status1, int status2, Pageable pageable);
}
