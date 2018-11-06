package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController
{
    @Autowired
    private BookService bookService;

    @RequestMapping(value = {"/searchBook", "/reader/searchBook"}, method = RequestMethod.GET)
    @ResponseBody
    public Page<Book> searchBook(String string,
                                 @RequestParam(value = "start", defaultValue = "0") int start,
                                 @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return bookService.searchBook(string, start, size);
    }

    @RequestMapping(path = {"/reader/showbook", "/showbook"}, method = RequestMethod.GET)
    @ResponseBody
    public Page<Book> showBook(@RequestParam(value = "start", defaultValue = "0") int start,
                               @RequestParam(value = "size", defaultValue = "10") int size, String category)
    {
        return bookService.showBook(start, size, category);
    }

    @RequestMapping(value = "/book_details", method = RequestMethod.GET)
    public String reader_book_details(String isbn, Model model)
    {
        model.addAttribute("book", bookService.bookInfo(isbn));
        return "book_details";
    }
}
