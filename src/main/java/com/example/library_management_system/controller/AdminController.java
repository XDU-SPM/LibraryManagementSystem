package com.example.library_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.library_management_system.service.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController
{
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalUtilService globalUtilService;

    @RequestMapping(value = "admin/librarian_create", method = RequestMethod.GET)
    public String admin_librarian_create(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/librarian_create";
    }

    @RequestMapping(value = "admin/permission_change", method = RequestMethod.GET)
    public String admin_permission_change(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        model.addAttribute("registerMoney", globalUtilService.getRegisterMoney());
        model.addAttribute("maxBorrowDays", globalUtilService.getMaxBorrowDays());
        model.addAttribute("maxBorrowNum", globalUtilService.getMaxBorrowNum());
        model.addAttribute("overdueMoney", globalUtilService.getOverdueMoney());
        return "admin/permission_change";
    }

    @RequestMapping(value = "/admin/modifyRegisterMoney", method = RequestMethod.POST)
    public String modifyRegisterMoney(double money, Model model)
    {
        adminService.modifyRegisterMoney(money);
        model.addAttribute("status", true);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:permission_change";
    }

    @RequestMapping(value = "/admin/modifyMaxBorrowDays", method = RequestMethod.POST)
    public String modifyMaxBorrowDays(int days, Model model)
    {
        adminService.modifyMaxBorrowDays(days);
        model.addAttribute("status", true);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:permission_change";
    }

    @RequestMapping(value = "/admin/modifyMaxBorrowNum", method = RequestMethod.POST)
    public String modifyMaxBorrowNum(int num, Model model)
    {
        adminService.modifyMaxBorrowNum(num);
        model.addAttribute("status", true);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:permission_change";
    }

    @RequestMapping(value = "/admin/modifyOverdueMoney", method = RequestMethod.POST)
    public String modifyOverdueMoney(double money, Model model)
    {
        adminService.modifyOverdueMoney(money);
        model.addAttribute("status", true);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:permission_change";
    }

    @RequestMapping(value = "/admin/modifyPassword", method = RequestMethod.POST)
    public String modifyPassword(String oldPassword, String newPassword, Model model)
    {
        model.addAttribute("status", userService.modifyPassword(oldPassword, newPassword) ? 1 : 0);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/password_change";
    }

    @RequestMapping(value = "/admin/librarian_edit", method = RequestMethod.GET)
    public String librarian_edit(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/librarian_edit";
    }

    @RequestMapping(value = "/admin/password_change", method = RequestMethod.GET)
    public String password_change(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/password_change";
    }

    @RequestMapping(value = "/admin/retrieve_password", method = RequestMethod.GET)
    public String retrieve_password(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/retrieve_password";
    }
}
