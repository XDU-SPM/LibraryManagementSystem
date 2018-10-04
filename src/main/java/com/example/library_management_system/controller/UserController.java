package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.Message;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main()
    {
        return "visitor-main";
    }

    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String login()
    {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))  // Has logged
        {
            User user = userService.getUser();
            if (user.getRoles().contains(RoleUtil.ROLE_ADMIN))
            {
                System.out.println("admin has logged in");
                return "redirect:admin/home";
            }
            else if (user.getRoles().contains(RoleUtil.ROLE_READER))
            {
                System.out.println("reader has logged in");
                return "redirect:reader/home";
            }
            else
            {
                System.out.println("librarian has logged in");
                return "redirect:librarian/home";
            }
        }
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register()
    {
        return "register";
    }

    @RequestMapping(value = "/reader/home", method = RequestMethod.GET)
    public String readerHome()
    {
        return "reader/reader_condition";
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public String adminHome()
    {
        return "admin/admin_homepage";
    }

    @RequestMapping(value = "/librarian/home", method = RequestMethod.GET)
    public String librarianHome()
    {
        return "librarian/librarian_homepage";
    }

    @RequestMapping(value = "/reader/register", method = RequestMethod.POST)
    public String readerRegister(User reader)
    {
        userService.registerService(reader, RoleUtil.ROLE_READER_CHECK);
        return "login";
    }

    @RequestMapping(value = "/admin/reader_register", method = RequestMethod.POST)
    public String adminReaderRegister(User reader, Model model)
    {
        userService.registerService(reader, RoleUtil.ROLE_READER_CHECK);
        model.addAttribute("state", true);
        return "admin/reader_create";
    }

    @RequestMapping(value = "/admin/librarian_register", method = RequestMethod.POST)
    public String adminLibrarianRegister(User librarian, Model model)
    {
        userService.registerService(librarian, RoleUtil.ROLE_LIBRARIAN_CHECK);
        model.addAttribute("state", true);
        return "admin/librarian_create";
    }

    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    @ResponseBody
    public User adminRegister(User admin)
    {
        userService.registerService(admin, RoleUtil.ROLE_ADMIN);
        return admin;
    }

    @RequestMapping(value = "/librarian/register", method = RequestMethod.POST)
    @ResponseBody
    public User librarianRegister(User librarian)
    {
        userService.registerService(librarian, RoleUtil.ROLE_LIBRARIAN_CHECK);
        return librarian;
    }

    @RequestMapping(value = "/admin/accept", method = RequestMethod.GET)
    @ResponseBody
    public Message accept(int id)
    {
        String role = userService.accept(id);
        if (RoleUtil.ROLE_READER_CHECK.equals(role))
            return new Message("readerHome");
        else if (RoleUtil.ROLE_LIBRARIAN_CHECK.equals(role))
            return new Message("librarianHome");
        return null;
    }

    //用户续借图书
    @RequestMapping(value = "/user/renew", method = RequestMethod.GET)
    public String renew(int id)
    {
        if (userService.renew(id))
        {
            return "redirect:/readerquery";
        }
        return null;
    }


}
