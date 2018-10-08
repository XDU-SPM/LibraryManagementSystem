package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.*;
import com.example.library_management_system.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReaderController
{
    @Autowired
    private StatService statService;

    @Autowired
    private GlobalUtilService globalUtilService;

    @Autowired
    private ReaderFunctionService readerFunctionService;

    @Autowired
    private LibrarianBookService librarianBookService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/reader/reader_condition", method = RequestMethod.GET)
    public String reader_reader_condition(Model model)
    {
        model.addAttribute("borrowedNumber", globalUtilService.getMaxBorrowNum() - statService.getUserBUL());
        model.addAttribute("RemainNumber", statService.getUserBUL());
        model.addAttribute("monthBorrows", statService.monthborrow());
        model.addAttribute("page1", readerFunctionService.queryborrowedBooks(0, 5, 1));
        model.addAttribute("page2", readerFunctionService.queryborrowedBooks(0, 5, 2));
        model.addAttribute("page3", readerFunctionService.queryborrowedBooks(0, 5, 3));
        return "reader/reader_condition";
    }

    @RequestMapping(value = "/reader/reader_search", method = RequestMethod.GET)
    public String reader_reader_search(Model model)
    {
        model.addAttribute("page", librarianBookService.showbook(0, 8, null));
        return "reader/reader_search";
    }

    @RequestMapping(value = "/reader/reader_information", method = RequestMethod.GET)
    public String reader_reader_information(Model model)
    {
        model.addAttribute("user", userService.getUser());
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
        model.addAttribute("book", librarianBookService.bookInfo(isbn));
        return "reader/book_details";
    }

    @RequestMapping(value = "/reader/reader_comment", method = RequestMethod.GET)
    public String reader_reader_comment(String isbn, Model model)
    {
        model.addAttribute("isbn", isbn);
        return "reader/reader_comment";
    }

    @RequestMapping(value = "/reader/appointment", method = RequestMethod.GET)
    public String appointment(int id, Model model)
    {
        model.addAttribute("userBkunit", readerService.appointment(id));
        return "reader/appointment";
    }

    @RequestMapping(value = "reader/saveUser", method = RequestMethod.POST)
    public String saveUser(User user, Model model)
    {
        userService.saveUser(user);
        model.addAttribute("status", true);
        return "redirect:reader_information";
    }
}
