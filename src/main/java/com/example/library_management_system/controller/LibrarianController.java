package com.example.library_management_system.controller;

import com.example.library_management_system.service.LibrarianService;
import com.example.library_management_system.service.StatService;
import com.example.library_management_system.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LibrarianController
{
    @Autowired
    private LibrarianService librarianService;

    @Autowired
    private StatService statService;

    @RequestMapping(value = "/librarian/librarian_borrow", method = RequestMethod.GET)
    public String librarian_librarian_borrow()
    {
        return "librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/librarian_return", method = RequestMethod.GET)
    public String librarian_librarian_return()
    {
        return "librarian/librarian_return";
    }

    @RequestMapping(value = "/librarian/librarian_record", method = RequestMethod.GET)
    public String librarian_librarian_record()
    {
        return "librarian/librarian_record";
    }

    @RequestMapping(value = "/librarian/librarian_homepage", method = RequestMethod.GET)
    public String librarian_librarian_homepage(Model model)
    {
        model.addAttribute("todayBorrowNumber", statService.todayBorrowNumber());
        model.addAttribute("todayReturnNumber", statService.todayReturnNumber());
        model.addAttribute("todayOverdueNumber", statService.todayOverdueNumber());
        model.addAttribute("todayFineIncome", statService.todayFineIncome());
        long[] statusnum = statService.statusnum();
        model.addAttribute("borrowedNumber", statusnum[0]);
        model.addAttribute("availableNumber", statusnum[1]);
        model.addAttribute("reserveNumber", statusnum[2]);
        model.addAttribute("dayBorrowReturns", statService.dayBorrowReturns());
        return "librarian/librarian_homepage";
    }

    @RequestMapping(value = "/librarian/librarian_user", method = RequestMethod.GET)
    public String librarian_librarian_user()
    {
        return "librarian/librarian_user";
    }

    @RequestMapping(value = "/librarian/librarian_book", method = RequestMethod.GET)
    public String librarian_librarian_book()
    {
        return "librarian/librarian_book";
    }

    @RequestMapping(value = {"/librarian/checkUser", "/reader/checkUser"}, method = RequestMethod.GET)
    @ResponseBody
    public Status userExist(String username)
    {
        boolean status = librarianService.userExist(username);
        return new Status(status ? 0 : 1);
    }
}
