package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.LibrarianService;
import com.example.library_management_system.service.StatService;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LibrarianController
{
    @Autowired
    private LibrarianService librarianService;

    @Autowired
    private StatService statService;

    @Autowired
    private UserService userService;

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
    public String librarian_librarian_user(Model model)
    {
        model.addAttribute("user", userService.getUser());
        return "librarian/librarian_user";
    }

    @RequestMapping(value = {"/librarian/checkUser", "/reader/checkUser", "/checkUser"}, method = RequestMethod.GET)
    @ResponseBody
    public Status userExist(String username)
    {
        boolean status = librarianService.userExist(username);
        return new Status(status ? 0 : 1);
    }

    @RequestMapping(value = "/librarian/librarian_record", method = RequestMethod.GET)
    public String getReserves(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                              @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", librarianService.getReserves(start, size));
        return "librarian/librarian_record";
    }

    @RequestMapping(value = "librarian/saveUser", method = RequestMethod.POST)
    public String saveUser(User user, Model model)
    {
        userService.saveUser(user);
        model.addAttribute("status", true);
        return "redirect:librarian_user";
    }
}
