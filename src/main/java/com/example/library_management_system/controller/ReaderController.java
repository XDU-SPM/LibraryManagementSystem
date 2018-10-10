package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReaderController
{
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/reader/reader_search", method = RequestMethod.GET)
    public String reader_reader_search(Model model)
    {
        model.addAttribute("page", bookService.showBook(0, 8, null));
        return "reader/reader_search";
    }

    @RequestMapping(value = "/reader/reader_information", method = RequestMethod.GET)
    public String reader_reader_information(Model model)
    {
        User reader = userService.getUser();
        model.addAttribute("user", reader);
        return "reader/reader_information";
    }

    @RequestMapping(value = "/reader/reader_message", method = RequestMethod.GET)
    public String reader_reader_message()
    {
        return "reader/reader_message";
    }

    @RequestMapping(value = "/reader/book_details", method = RequestMethod.GET)
    public String reader_book_details(String isbn, Model model)
    {
        model.addAttribute("book", bookService.bookInfo(isbn));
        return "reader/book_details";
    }

    @RequestMapping(value = "/reader/reader_comment", method = RequestMethod.GET)
    public String reader_reader_comment(String isbn, Model model)
    {
        model.addAttribute("isbn", isbn);
        return "reader/reader_comment";
    }

    @RequestMapping(value = "reader/saveUser", method = RequestMethod.POST)
    public String saveUser(User user, Model model)
    {
        userService.saveUser(user);
        model.addAttribute("status", 1);
        return "redirect:reader_information";
    }
}
