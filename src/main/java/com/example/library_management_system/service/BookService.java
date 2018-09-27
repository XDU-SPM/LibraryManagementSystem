package com.example.library_management_system.service;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.dao.BookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookDAO bookDAO;

    public Book search(String name){
        return bookDAO.findByName(name);
    }
}
