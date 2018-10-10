package com.example.library_management_system.controller;

import com.example.library_management_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController
{
    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/searchBook", method = RequestMethod.GET)
    public String searchBook(String string, int type, Model model,
                             @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", bookService.searchBook(string, type, start, size));
        return "";
    }
}
