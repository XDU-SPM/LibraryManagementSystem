package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.AdminUserService;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminUserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/admin/librarian_register", method = RequestMethod.POST)
    public String adminLibrarianRegister(User librarian, Model model)
    {
        userService.registerService(librarian, RoleUtil.ROLE_LIBRARIAN_CHECK);
        model.addAttribute("status", true);
        return "admin/librarian_create";
    }

    @RequestMapping(value = "/admin/searchLibrarian", method = RequestMethod.GET)
    public String searchLibrarian(String username, Model model)
    {
        model.addAttribute("librarian", userService.searchUser(username));
        return "";
    }

    @RequestMapping(value = "admin/delete_users", method = RequestMethod.GET)
    public String admin_delete_users(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", userService.showAllUser(start, size, RoleUtil.ROLE_LIBRARIAN_CHECK));
        return "admin/delete_users";
    }

    @RequestMapping(value = "/admin/deleteaccount", method = RequestMethod.GET)
    public String deleteUser(int id, int start)
    {
        System.out.println(id);
        userService.deleteUser(id);
        return "redirect:delete_users?start=" + start;
    }

    @RequestMapping(value = "/admin/getLibrarianPassword", method = RequestMethod.GET)
    public String getLibrarianPassword(int id, Model model)
    {
        model.addAttribute("librarianPassword", adminUserService.getLibrarianPassword(id));
        return "";
    }
}
