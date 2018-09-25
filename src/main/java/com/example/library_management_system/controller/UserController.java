package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.Message;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "/login"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String login()
    {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))  // Has logged
        {
            User user = userService.getUser();
            if (user.getRoles().contains(RoleUtil.ROLE_ADMIN))
                return "redirect:admin/home";
            else if (user.getRoles().contains(RoleUtil.ROLE_READER))
                return "redirect:reader/home";
            else
                return "redirect:librarian/home";
        }
        return "login";
    }

    @RequestMapping(value = "/reader/home", method = RequestMethod.GET)
    @ResponseBody
    public Message readerHome()
    {
        return new Message("reader/home");
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    @ResponseBody
    public Message adminHome()
    {
        return new Message("admin/home");
    }

    @RequestMapping(value = "/librarian/home", method = RequestMethod.GET)
    @ResponseBody
    public Message librarianHome()
    {
        return new Message("librarian/home");
    }

    @RequestMapping(value = "/reader/register", method = RequestMethod.POST)
    @ResponseBody
    public User readerRegister(User student)
    {
        userService.registerService(student, RoleUtil.ROLE_READER);
        return student;
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    @ResponseBody
    public User adminRegister(User admin)
    {
        userService.registerService(admin, RoleUtil.ROLE_ADMIN);
        return admin;
    }

    @RequestMapping(value = "/librarian/register", method = RequestMethod.POST)
    @ResponseBody
    public User librarianRegister(User librarian)
    {
        userService.registerService(librarian, RoleUtil.ROLE_ADMIN);
        return librarian;
    }
}
