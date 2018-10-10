package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.GlobalUtilService;
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

    @Autowired
    private GlobalUtilService globalUtilService;

    @RequestMapping(value = "/librarian/readerRegister", method = RequestMethod.POST)
    public String readerRegister(User reader, Model model)
    {
        userService.registerService(reader, RoleUtil.ROLE_READER_CHECK);
        model.addAttribute("status", true);
        return "/librarian/librarian_create";
    }

    @RequestMapping(value = "/librarian/payFine", method = RequestMethod.POST)
    public String payFine(double money, String username, Model model)
    {
        model.addAttribute("status", librarianUserService.payFine(money, username));
        return "";
    }

    @RequestMapping(value = "/librarian/librarian_income", method = RequestMethod.GET)
    public String librarian_income()
    {
        return "librarian/librarian_income";
    }

    @RequestMapping(value = "/librarian/librarian_fine", method = RequestMethod.GET)
    public String librarian_fine()
    {
        return "librarian/librarian_fine";
    }

    @RequestMapping(value = "/librarian/librarian_addreader", method = RequestMethod.GET)
    public String librarian_addreader(Model model)
    {
        model.addAttribute("registerMoney", globalUtilService.getRegisterMoney());
        return "librarian/librarian_addreader";
    }

    @RequestMapping(value = "/librarian/librarian_managereader", method = RequestMethod.GET)
    public String librarian_managereader()
    {
        return "librarian/librarian_managereader";
    }

    @RequestMapping(value = "/librarian/deleteReader", method = RequestMethod.GET)
    public String deleteReader(int id, Model model)
    {
        model.addAttribute("status", userService.deleteUser(id));
        return "";
    }
}
