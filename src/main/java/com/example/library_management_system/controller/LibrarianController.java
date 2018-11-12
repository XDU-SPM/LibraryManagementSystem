package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Announcement;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.*;
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
    private UserService userService;

    @Autowired
    private LibrarianBookService librarianBookService;

    @Autowired
    private LibrarianUserService librarianUserService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocationService locationService;

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

    @RequestMapping(value = "librarian/saveReader", method = RequestMethod.POST)
    public String saveReader(User user, Model model)
    {
        librarianUserService.saveReader(user);
        model.addAttribute("status", true);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:librarian_readerinfo?id=" + user.getId();
    }

    @RequestMapping(value = "/librarian/saveUser", method = RequestMethod.POST)
    public String saveUser(User user)
    {
        userService.saveUser(user);
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

    @RequestMapping(value = "/librarian/modifyAnnouncement", method = RequestMethod.POST)
    public String modifyAnnouncement(Announcement announcement)
    {
        announcementService.modifyAnnouncement(announcement);
        return "redirect:librarian_announce";
    }

    @RequestMapping(value = "/librarian/deleteAnnouncement", method = RequestMethod.GET)
    @ResponseBody
    public Status deleteAnnouncement(int id)
    {
        announcementService.deleteAnnouncement(id);
        return new Status(1);
    }

    @RequestMapping(value = "/librarian/modifyPassword", method = RequestMethod.POST)
    public String modifyPassword(String oldPassword, String newPassword, Model model)
    {
        model.addAttribute("status", userService.modifyPassword(oldPassword, newPassword) ? 1 : 0);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/password_change";
    }

    @RequestMapping(value = "librarian/password_change", method = RequestMethod.GET)
    public String password_change(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/password_change";
    }

    @RequestMapping(value = "/librarian/librarian_message", method = RequestMethod.GET)
    public String librarian_message(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        model.addAttribute("set", librarianUserService.getMessages());
        return "librarian/librarian_message";
    }

    @RequestMapping(value = "/librarian/librarian_category", method = RequestMethod.GET)
    public String librarian_category(Model model)
    {
        model.addAttribute("categories", categoryService.categories());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_category";
    }

    @RequestMapping(value = "/librarian/librarian_position", method = RequestMethod.GET)
    public String librarian_position(Model model)
    {
        model.addAttribute("locations", locationService.locations());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_position";
    }

    @RequestMapping(value = "/librarian/addLocation", method = RequestMethod.POST)
    public String addLocation(String name)
    {
        locationService.addLocation(name);
        return "redirect:librarian_position";
    }

    @RequestMapping(value = "/librarian/removeLocation", method = RequestMethod.GET)
    @ResponseBody
    public Status removeLocation(int id)
    {
        locationService.removeLocation(id);
        return new Status(1);
    }

    @RequestMapping(value = "/librarian/c", method = RequestMethod.POST)
    public String updateLocation(int id, String name)
    {
        System.out.println(id + " " + name);
        locationService.updateLocation(id, name);
        return "redirect:librarian_position";
    }

    @RequestMapping(value = "/librarian/addCategory", method = RequestMethod.POST)
    public String addCategory(String name)
    {
        categoryService.addCategory(name);
        return "redirect:librarian_category";
    }

    @RequestMapping(value = "/librarian/removeCategory", method = RequestMethod.GET)
    @ResponseBody
    public Status removeCategory(int id)
    {
        categoryService.removeCategory(id);
        return new Status(1);
    }
}
