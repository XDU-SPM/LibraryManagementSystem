package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.bean.UserFavoriteBook;
import com.example.library_management_system.service.ReaderFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ author Captain
 * @ date 2018/9/26
 * @ description as bellow.
 * 读者(用户)的个人功能，比如查询已借阅图书、管理(增/删/查)收藏夹图书
 */
@Controller
public class ReaderFunctionController
{
    @Autowired
    private ReaderFunctionService readerfunctionservice;

    @RequestMapping(value = "/reader/borrowedBooks", method = RequestMethod.GET)
    public String queryBorrowedBooks(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Page<UserBkunit> page = readerfunctionservice.queryborrowedBooks(start, size);
        model.addAttribute("page", page);
        return "queryBorrowedBooks";
    }

    @RequestMapping(value = "/reader/favoriteBooks", method = RequestMethod.GET)
    public String queryFavoriteBooks(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Page<UserFavoriteBook> page = readerfunctionservice.queryFavoriteBooks(start, size);
        model.addAttribute("page", page);
        return "queryFavoriteBooks";
    }

    @RequestMapping(value = "/reader/addFavoriteBooks", method = RequestMethod.GET)
    public String addFavoriteBook(Model model, Book book)
    {
        if (!readerfunctionservice.addFavoriteBook(book))
            model.addAttribute("state", 0);
        else
            model.addAttribute("state", 1);
        return "addFavoriteBook";
    }

    @RequestMapping(value = "/reader/deleteFavoriteBooks", method = RequestMethod.GET)
    public String deleteFavoriteBook(Model model, Book book)
    {
        if (!readerfunctionservice.deleteFavoriteBook(book))
            model.addAttribute("state", 0);
        else
            model.addAttribute("state", 1);
        return "deleteFavoriteBook";
    }
}
