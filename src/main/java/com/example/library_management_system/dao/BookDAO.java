package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.Category;
import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDAO extends JpaRepository<Book, String>
{
    Book findByIsbn(String isbn);

    Page<Book> findByIsbnLike(String isbn, Pageable pageable);

    Page<Book> findByTitleLike(String title, Pageable pageable);

    Page<Book> findByAuthorLike(String author, Pageable pageable);

    Page<Book> findByCategoriesContaining(Category category, Pageable pageable);

    Page<Book> findAll(Pageable pageable);
}
