package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Book  search(HttpServletRequest request){
        String bookname=request.getParameter("bookname" );
        return bookService.search(bookname);
    }


}
