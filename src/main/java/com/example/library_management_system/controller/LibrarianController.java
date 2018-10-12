package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Announcement;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.AnnouncementService;
import com.example.library_management_system.service.LibrarianBookService;
import com.example.library_management_system.service.LibrarianUserService;
import com.example.library_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LibrarianController
{
    @Autowired
    private UserService userService;

    @Autowired
    private LibrarianBookService librarianBookService;

    @Autowired
    private LibrarianUserService librarianUserService;

    @Autowired
    private AnnouncementService announcementService;

    @RequestMapping(value = "/librarian/librarian_borrow", method = RequestMethod.GET)
    public String librarian_librarian_borrow(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/librarian_return", method = RequestMethod.GET)
    public String librarian_librarian_return(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_return";
    }

    @RequestMapping(value = "/librarian/librarian_homepage", method = RequestMethod.GET)
    public String librarian_librarian_homepage(Model model)
    {
        model.addAttribute("todayBorrowNumber", librarianBookService.todayBorrowNumber());
        model.addAttribute("todayReturnNumber", librarianBookService.todayReturnNumber());
        model.addAttribute("todayOverdueNumber", librarianBookService.todayOverdueNumber());
        model.addAttribute("todayFineIncome", librarianUserService.todayFineIncome());
        long[] statusNum = librarianBookService.statusNum();
        model.addAttribute("borrowedNumber", statusNum[0]);
        model.addAttribute("availableNumber", statusNum[1]);
        model.addAttribute("reserveNumber", statusNum[2]);
        model.addAttribute("dayBorrowReturns", librarianBookService.dayBorrowReturns());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_homepage";
    }

    @RequestMapping(value = "/librarian/librarian_user", method = RequestMethod.GET)
    public String librarian_librarian_user(Model model)
    {
        model.addAttribute("user", userService.getUser());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_user";
    }

    @RequestMapping(value = "librarian/saveUser", method = RequestMethod.POST)
    public String saveUser(User user, Model model)
    {
        userService.saveUser(user);
        model.addAttribute("status", true);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:librarian_user";
    }

    @RequestMapping(value = "/librarian/librarian_announce", method = RequestMethod.GET)
    public String librarian_announce(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        model.addAttribute("set", announcementService.getAnnouncements());
        return "librarian/librarian_announce";
    }

    @RequestMapping(value = "/librarian/addAnnouncement", method = RequestMethod.POST)
    public String addAnnouncement(Announcement announcement, Model model)
    {
        announcementService.addAnnouncement(announcement);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        model.addAttribute("set", announcementService.getAnnouncements());
        model.addAttribute("status", true);
        return "librarian/librarian_announce";
    }
}
