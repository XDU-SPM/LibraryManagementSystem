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
    private AdminService adminService;

    /*@RequestMapping(value = "admin/reader_create", method = RequestMethod.GET)
    public String admin_reader_create()
    {
        return "admin/reader_create";
    }*/

    @RequestMapping(value = "admin/librarian_create", method = RequestMethod.GET)
    public String admin_librarian_create()
    {
        return "admin/librarian_create";
    }

    @RequestMapping(value = "admin/permission_change", method = RequestMethod.GET)
    public String admin_permission_change()
    {
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyRegisterMoney", method = RequestMethod.POST)
    public String modifyRegisterMoney(double money, Model model)
    {
        adminService.modifyRegisterMoney(money);
        model.addAttribute("status", true);
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyMaxBorrowDays", method = RequestMethod.POST)
    public String modifyMaxBorrowDays(int days, Model model)
    {
        adminService.modifyMaxBorrowDays(days);
        model.addAttribute("status", true);
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyMaxBorrowNum", method = RequestMethod.POST)
    public String modifyMaxBorrowNum(int num, Model model)
    {
        adminService.modifyMaxBorrowNum(num);
        model.addAttribute("status", true);
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyOverdueMoney", method = RequestMethod.POST)
    public String modifyOverdueMoney(double money, Model model)
    {
        adminService.modifyOverdueMoney(money);
        model.addAttribute("status", true);
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyPassword", method = RequestMethod.POST)
    public String modifyPassword(String oldPassword, String newPassword, Model model)
    {
        model.addAttribute("status", adminService.modifyPassword(oldPassword, newPassword));
        return "";
    }

    @RequestMapping(value = "/admin/librarian_edit", method = RequestMethod.GET)
    public String librarian_edit()
    {
        return "admin/librarian_edit";
    }

    @RequestMapping(value = "/admin/password_change", method = RequestMethod.GET)
    public String password_change()
    {
        return "admin/password_change";
    }
}
