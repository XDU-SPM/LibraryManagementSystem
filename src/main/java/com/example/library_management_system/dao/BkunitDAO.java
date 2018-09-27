package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Bkunit;
import com.example.library_management_system.bean.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BkunitDAO extends JpaRepository<Bkunit, String> {

    Page<Bkunit> findAll(Pageable pageable);
    Bkunit findByBook(Book book);
    long countAllByBook(Book book);
    long countAllByBookAndAndStatus(Book book,int status);
}
