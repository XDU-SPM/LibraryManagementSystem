package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BookController
{
    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/reader/search", method = RequestMethod.GET)
    public Book search(String bookName)
    {
        return bookService.search(bookName);
    }
}
