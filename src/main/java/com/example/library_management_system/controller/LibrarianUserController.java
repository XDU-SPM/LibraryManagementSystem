package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Income;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Set;

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
        model.addAttribute("registerMoney", globalUtilService.getRegisterMoney());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "/librarian/librarian_addreader";
    }

    @RequestMapping(value = "/librarian/payFine", method = RequestMethod.POST)
    public String payFine(double money, String username, Model model)
    {
        model.addAttribute("status", librarianUserService.payFine(money, username));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "";
    }

    @RequestMapping(value = "/librarian/librarian_income", method = RequestMethod.GET)
    public String librarian_income(Model model)
    {
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        model.addAttribute("set", librarianUserService.getDayIncome());
        model.addAttribute("set2", librarianUserService.getWeekIncome());
        return "librarian/librarian_income";
    }

    @RequestMapping(value = "/librarian/librarian_addreader", method = RequestMethod.GET)
    public String librarian_addreader(Model model)
    {
        model.addAttribute("registerMoney", globalUtilService.getRegisterMoney());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_addreader";
    }

    @RequestMapping(value = "/librarian/librarian_managereader", method = RequestMethod.GET)
    public String librarian_managereader(Model model)
    {
        model.addAttribute("set", userService.showAllUser(RoleUtil.ROLE_READER_CHECK));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_managereader";
    }

    @RequestMapping(value = "/librarian/librarian_readerinfo", method = RequestMethod.GET)
    public String librarian_readerinfo(Model model, int id)
    {
        model.addAttribute("user", userService.showUser(id));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "/librarian/librarian_readerinfo";
    }

    @RequestMapping(value = "/librarian/librarian_fine", method = RequestMethod.GET)
    public String getPaidAccounts(int uid, Model model)
    {
        model.addAttribute("set", librarianUserService.getPaidAccounts(uid));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_fine";
    }

    @RequestMapping(value = "/librarian/librarian_fine2", method = RequestMethod.GET)
    public String getUnPaidAccounts(int uid, Model model)
    {
        model.addAttribute("set", librarianUserService.getUnPaidAccounts(uid));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_fine2";
    }

    @RequestMapping(value = "/librarian/getMonthIncome", method = RequestMethod.GET)
    @ResponseBody
    public Set<Income> getMonthIncome(int year)
    {
        return librarianUserService.getMonthIncome(year);
    }

    @RequestMapping(value = "/librarian/getWeekIncome", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Income> getWeekIncome()
    {
        return librarianUserService.getWeekIncome();
    }
}
