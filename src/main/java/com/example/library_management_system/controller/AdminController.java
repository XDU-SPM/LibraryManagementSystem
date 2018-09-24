package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Admin;
import com.example.library_management_system.service.AdminService;
import com.example.library_management_system.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController
{
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = {"/admin/", "/admin/login"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String login()
    {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
        {
            System.out.println("admin/home");
            return "redirect:/home";
        }
        System.out.println("admin/login");
        return "admin/login";
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    @ResponseBody
    public Message home()
    {
        return new Message("admin/home");
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    @ResponseBody
    public Admin register(Admin admin)
    {
        adminService.registerService(admin);
        return admin;
    }
}
