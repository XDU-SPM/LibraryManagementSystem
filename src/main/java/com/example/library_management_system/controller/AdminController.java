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

@Controller
public class AdminController
{
    @Autowired
    private AdminService adminservice;

    @RequestMapping(path = "/admin/deleteaccount", method = {RequestMethod.POST})
    public String deleteuser(@RequestParam(value = "id") int id)
    {
        adminservice.deleteuser(id);
        return "admin";
    }

    @RequestMapping(path = "/admin", method = {RequestMethod.GET})
    public String showallinfo(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @RequestParam(value = "role") String role)
    {
        Page<User> page = adminservice.showallinfo(start, size, role);
        model.addAttribute("page", page);
        return "userinfo";
    }

    @RequestMapping(path = "/admin/showinfo", method = {RequestMethod.GET})
    public String showinfo(@RequestParam(value = "id") int id, Model model)
    {
        model.addAttribute("accountinfo", adminservice.showinfo(id));
        return "userinfo";
    }

    @RequestMapping(value = "/admin/modifyRegisterMoney", method = RequestMethod.POST)
    public String modifyRegisterMoney(double money, Model model)
    {
        adminservice.modifyRegisterMoney(money);
        model.addAttribute("status", true);
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyMaxBorrowDays", method = RequestMethod.POST)
    public String modifyMaxBorrowDays(int days, Model model)
    {
        adminservice.modifyMaxBorrowDays(days);
        model.addAttribute("status", true);
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyMaxBorrowNum", method = RequestMethod.POST)
    public String modifyMaxBorrowNum(int num, Model model)
    {
        adminservice.modifyMaxBorrowNum(num);
        model.addAttribute("status", true);
        return "admin/permission_change";
    }
}
