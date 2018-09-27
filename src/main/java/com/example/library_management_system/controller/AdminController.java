package com.example.library_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import com.example.library_management_system.service.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.library_management_system.bean.*;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;

@Controller
public class AdminController
{
    @Autowired
    private AdminService adminservice;

    @RequestMapping(path = "/admin/deleteaccount", method = {RequestMethod.POST})
    public String deleteuser(User user)
    {
        adminservice.deleteuser(user);
        return "admin";
    }

    @RequestMapping(path = "/admin", method = {RequestMethod.GET})
    public String showallinfo(Model model, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) throws Exception
    {
        Page<User> page = adminservice.showallinfo(start, size);
        model.addAttribute("page", page);
        return "userinfo";
    }

    @RequestMapping(path = "/admin/showinfo", method = {RequestMethod.POST})
    public String showinfo(@PathParam(value = "id") String id, Model model)
    {
        model.addAttribute("accountinfo", adminservice.showinfo(Integer.parseInt(id)));
        return "userinfo";
    }

    @RequestMapping(path = "/admin/changeinfo", method = {RequestMethod.GET})
    public String changeinfo(User user)
    {
        adminservice.changeinfo(user);
        return "userinfo";
    }
}
