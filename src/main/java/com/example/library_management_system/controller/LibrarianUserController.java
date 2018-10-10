package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.LibrarianUserService;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LibrarianUserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private LibrarianUserService librarianUserService;

    @RequestMapping(value = "/librarian/readerRegister", method = RequestMethod.POST)
    public String readerRegister(User reader, Model model)
    {
        userService.registerService(reader, RoleUtil.ROLE_READER_CHECK);
        model.addAttribute("status", true);
        return "";
    }

    @RequestMapping(value = "/librarian/payFine", method = RequestMethod.POST)
    public String payFine(double money, String username, Model model)
    {
        model.addAttribute("status", librarianUserService.payFine(money, username));
        return "";
    }
}
