package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Review;
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
    @ResponseBody
    public Page<UserBkunit> queryBorrowedBooks(@RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size, int status)
    {
        return readerfunctionservice.queryborrowedBooks(start, size, status);
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
    public String addFavoriteBook(Model model, String Isbn)
    {
        if (!readerfunctionservice.addFavoriteBook(Isbn))
            model.addAttribute("state", 0);
        else
            model.addAttribute("state", 1);
        return "addFavoriteBook";
    }

    @RequestMapping(value = "/reader/deleteFavoriteBooks", method = RequestMethod.GET)
    public String deleteFavoriteBook(Model model, String Isbn)
    {
        if (!readerfunctionservice.deleteFavoriteBook(Isbn))
            model.addAttribute("state", 0);
        else
            model.addAttribute("state", 1);
        return "deleteFavoriteBook";
    }

    @RequestMapping(value = "/librarian/lend", method = RequestMethod.POST)
    public String lend(Model model, String id, String username)
    {
        int status = readerfunctionservice.lend(id, username);
        model.addAttribute("status", status);
        return "/librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/return", method = RequestMethod.POST)
    public String returnBook(Model model, String id, @RequestParam(value = "damage", defaultValue = "0") int damage)
    {
        int status = readerfunctionservice.returnBook(id, damage);
        model.addAttribute("status", status);
        return "/librarian/librarian_return";
    }

    @RequestMapping(value = "/reader/writeReview", method = RequestMethod.POST)
    public String writeReview(Model model, String Isbn, String review, String title)
    {
        int state = readerfunctionservice.writeReview(Isbn, review, title);
        model.addAttribute("status", state);
        return "reader/reader_comment";
    }

    @RequestMapping(value = "/reader/bookReview", method = RequestMethod.GET)
    public String BookReview(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "size", defaultValue = "10") int size, String isbn)
    {
        Page<Review> page = readerfunctionservice.bookReview(start, size, isbn);
        model.addAttribute("page", page);
        return "bookReview";
    }

    @RequestMapping(value = "/reader/deleteReview", method = RequestMethod.POST)
    public String deleteReview(Model model, int rid)
    {
        boolean status = readerfunctionservice.deleteReview(rid);
        model.addAttribute("status", status);
        return "deleteReview";
    }

    @RequestMapping(value = "/reader/reserve", method = RequestMethod.GET)
    @ResponseBody
    public String reserveBook(String isbn)
    {
        String status = readerfunctionservice.reserve(isbn);
        return status;
    }

    @RequestMapping(value = "/reader/reserveCancel", method = RequestMethod.GET)
    public String reserveCancel(String id)
    {
        readerfunctionservice.reserveCancel(id);
        return "";
    }
}
