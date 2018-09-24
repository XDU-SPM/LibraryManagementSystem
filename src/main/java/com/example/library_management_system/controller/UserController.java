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
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
        {
            User user = userService.getUser();
            if (user.getRoles().contains(RoleUtil.ROLE_ADMIN))
                return "redirect:admin/home";
            else
                return "redirect:student/home";

        }
        return "login";
    }

    @RequestMapping(value = "/student/home", method = RequestMethod.GET)
    @ResponseBody
    public Message studentHome()
    {
        return new Message("student/home");
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    @ResponseBody
    public Message adminHome()
    {
        return new Message("admin/home");
    }

    @RequestMapping(value = "/student/register", method = RequestMethod.POST)
    @ResponseBody
    public User studentRegister(User student)
    {
        userService.registerService(student, RoleUtil.ROLE_STUDENT);
        return student;
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    @ResponseBody
    public User register(User admin)
    {
        userService.registerService(admin, RoleUtil.ROLE_ADMIN);
        return admin;
    }
}
