package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Message;
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

    @Autowired
    private ReaderBookService readerBookService;

    @Autowired
    private AnnouncementService announcementService;

    @RequestMapping(value = "/reader/reader_search", method = RequestMethod.GET)
    public String reader_reader_search(Model model)
    {
        model.addAttribute("page", bookService.showBook(0, 12, null));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "reader/reader_search";
    }

    @RequestMapping(value = "/reader/reader_information", method = RequestMethod.GET)
    public String reader_reader_information(Model model)
    {
        User reader = userService.getUser();
        model.addAttribute("user", reader);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "reader/reader_information";
    }

    @RequestMapping(value = "/reader/reader_message", method = RequestMethod.GET)
    public String reader_reader_message(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "reader/reader_message";
    }

    @RequestMapping(value = "/reader/book_details", method = RequestMethod.GET)
    public String reader_book_details(String isbn, Model model)
    {
        model.addAttribute("book", bookService.bookInfo(isbn));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        model.addAttribute("status", readerBookService.isFavoriteBook(isbn) ? 0 : 1);
        return "reader/book_details";
    }

    @RequestMapping(value = "/reader/reader_comment", method = RequestMethod.GET)
    public String reader_reader_comment(String isbn, Model model)
    {
        model.addAttribute("isbn", isbn);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "reader/reader_comment";
    }

    @RequestMapping(value = "/reader/saveUser", method = RequestMethod.POST)
    public String saveUser(User user, Model model)
    {
        userService.saveUser(user);
        model.addAttribute("status", 1);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:reader_information";
    }

    @RequestMapping(value = "/reader/modifyPassword", method = RequestMethod.POST)
    public String modifyPassword(String oldPassword, String newPassword, Model model)
    {
        model.addAttribute("status", userService.modifyPassword(oldPassword, newPassword) ? 1 : 0);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "reader/password_change";
    }

    @RequestMapping(value = "/reader/password_change", method = RequestMethod.GET)
    public String password_change(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "reader/password_change";
    }

    @RequestMapping(value = "/reader/Notice", method = RequestMethod.GET)
    public String Notice(int id, Model model)
    {
        model.addAttribute("announcement", announcementService.getAnnouncement(id));
        return "reader/Notice";
    }

    @RequestMapping(value = "/reader/addMessage", method = RequestMethod.POST)
    public String addMessage(Message message)
    {
        readerBookService.addMessage(message);
        return "redirect:reader_message";
    }
}
