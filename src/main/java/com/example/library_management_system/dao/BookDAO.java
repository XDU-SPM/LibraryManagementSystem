package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDAO extends JpaRepository<Book, String>
{
<<<<<<< HEAD
    Book findByIsbn(String isbn);
=======

>>>>>>> 260c39541dc44db52eed4f8d6eca71806b074a0a
}
