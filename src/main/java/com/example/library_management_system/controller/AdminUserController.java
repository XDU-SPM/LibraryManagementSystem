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
import org.springframework.web.bind.annotation.ResponseBody;

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
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/librarian_create";
    }

    @RequestMapping(value = "/admin/searchLibrarian", method = RequestMethod.GET)
    @ResponseBody
    public User searchLibrarian(String username)
    {
        return userService.searchUser(username);
    }

    @RequestMapping(value = "admin/delete_users", method = RequestMethod.GET)
    public String admin_delete_users(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", userService.showAllUser(start, size, RoleUtil.ROLE_LIBRARIAN_CHECK));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/delete_users";
    }

    @RequestMapping(value = "/admin/deleteaccount", method = RequestMethod.GET)
    public String deleteUser(int id, int start, Model model)
    {
        System.out.println(id);
        userService.deleteUser(id);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:delete_users?start=" + start;
    }

    @RequestMapping(value = "/admin/getLibrarianPassword", method = RequestMethod.GET)
    public String getLibrarianPassword(int id, Model model)
    {
        model.addAttribute("librarianPassword", adminUserService.getLibrarianPassword(id));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "";
    }

    @RequestMapping(value = "/admin/saveLibrarian", method = RequestMethod.POST)
    public String saveLibrarian(User librarian, Model model)
    {
        adminUserService.saveLibrarian(librarian);
        model.addAttribute("status", true);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "admin/librarian_edit";
    }
}
