package com.example.library_management_system.controller;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.service.*;
import com.example.library_management_system.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReaderBookController
{
    @Autowired
    private GlobalUtilService globalUtilService;

    @Autowired
    private ReaderBookService readerBookService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/reader/reader_condition", method = RequestMethod.GET)
    public String reader_reader_condition(Model model)
    {
        int borrowNumber = userService.getUser().getBorrowNumber();
        model.addAttribute("borrowedNumber", borrowNumber);
        model.addAttribute("RemainNumber", globalUtilService.getMaxBorrowNum() - borrowNumber);
        model.addAttribute("monthBorrows", readerBookService.monthBorrows());
        model.addAttribute("page1", readerBookService.queryBorrowedBooks(0, 5));
        model.addAttribute("page2", readerBookService.queryReservedBooks(0, 5));
        model.addAttribute("page3", readerBookService.queryReturnedBooks(0, 5));
        return "reader/reader_condition";
    }

    @RequestMapping(value = "/reader/borrowedBooks", method = RequestMethod.GET)
    @ResponseBody
    public Page<UserBkunit> queryBorrowedBooks(@RequestParam(value = "start", defaultValue = "0") int start,
                                               @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return readerBookService.queryBorrowedBooks(start, size);
    }
    @RequestMapping(value = "/reader/reservedBooks", method = RequestMethod.GET)
    @ResponseBody
    public Page<UserBook> queryReservedBooks(@RequestParam(value = "start", defaultValue = "0") int start,
                                             @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return readerBookService.queryReservedBooks(start, size);
    }

    @RequestMapping(value = "/reader/returnedBooks", method = RequestMethod.GET)
    @ResponseBody
    public Page<ReturnHistory> queryReturnedBooks(@RequestParam(value = "start", defaultValue = "0") int start,
                                                  @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return readerBookService.queryReturnedBooks(start, size);
    }

    @RequestMapping(value = "/reader/reserve", method = RequestMethod.GET)
    @ResponseBody
    public Status reserveBook(String isbn)
    {
        readerBookService.reserve(isbn);
        return new Status(1);
    }

    @RequestMapping(value = "/reader/reserveCancel", method = RequestMethod.GET)
    @ResponseBody
    public Status reserveCancel(int id)
    {
        return new Status(readerBookService.reserveCancel(id));
    }

    @RequestMapping(value = "/reader/appointment", method = RequestMethod.GET)
    public String appointment(int id, Model model)
    {
        model.addAttribute("userBkunit", readerBookService.appointment(id));
        return "reader/appointment";
    }

    @RequestMapping(value = "/reader/favoriteBooks", method = RequestMethod.GET)
    public String queryFavoriteBooks(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Page<UserFavoriteBook> page = readerBookService.queryFavoriteBooks(start, size);
        model.addAttribute("page", page);
        return "queryFavoriteBooks";
    }

    @RequestMapping(value = "/reader/addFavoriteBooks", method = RequestMethod.GET)
    public String addFavoriteBook(Model model, String isbn)
    {
        readerBookService.addFavoriteBook(isbn);
        model.addAttribute("status", true);
        return "addFavoriteBook";
    }

    @RequestMapping(value = "/reader/deleteFavoriteBooks", method = RequestMethod.GET)
    public String deleteFavoriteBook(Model model, String isbn)
    {
        readerBookService.deleteFavoriteBook(isbn);
        model.addAttribute("state", true);
        return "deleteFavoriteBook";
    }

    @RequestMapping(value = "/reader/writeReview", method = RequestMethod.POST)
    public String writeReview(Model model, String isbn, String review, String title)
    {
        readerBookService.writeReview(isbn, review, title);
        model.addAttribute("status", true);
        return "reader/reader_comment";
    }

    @RequestMapping(value = "/reader/bookReview", method = RequestMethod.GET)
    public String bookReview(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "size", defaultValue = "10") int size, String isbn)
    {
        Page<Review> page = readerBookService.bookReview(start, size, isbn);
        model.addAttribute("page", page);
        return "bookReview";
    }

    @RequestMapping(value = "/reader/deleteReview", method = RequestMethod.POST)
    public String deleteReview(Model model, int rid)
    {
        readerBookService.deleteReview(rid);
        model.addAttribute("status", true);
        return "deleteReview";
    }
}
