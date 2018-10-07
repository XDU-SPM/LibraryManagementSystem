package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Bkunit;
import com.example.library_management_system.bean.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface BkunitDAO extends JpaRepository<Bkunit, String>
{

    Page<Bkunit> findAll(Pageable pageable);

    Bkunit findByBook(Book book);

    Set<Bkunit> findAllByBookAndStatus(Book book, int status);

    long countAllByBook(Book book);

    long countAllByBookAndStatus(Book book, int status);

    long countByBook(Book book);

    long countByBookAndStatus(Book book, int status);

    long countByStatus(int status);
}
