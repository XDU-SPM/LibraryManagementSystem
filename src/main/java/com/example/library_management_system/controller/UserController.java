package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.BookService;
import com.example.library_management_system.service.GlobalUtilService;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.RoleUtil;
import com.example.library_management_system.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private GlobalUtilService globalUtilService;

    @Autowired
    private BookService bookService;

    @RequestMapping(value = {"/", "/visitor-main"}, method = RequestMethod.GET)
    public String main(Model model)
    {
        model.addAttribute("page", bookService.showBook(0, 8, null));
        return "visitor-main";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public String login()
    {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))  // Has logged in
        {
            Set<Role> roles = userService.getUser().getRoles();
            if (roleContainsString(roles, RoleUtil.ROLE_ADMIN))
                return "redirect:admin/home";
            else if (roleContainsString(roles, RoleUtil.ROLE_READER) || roleContainsString(roles, RoleUtil.ROLE_READER_CHECK))
                return "redirect:reader/home";
            else if (roleContainsString(roles, RoleUtil.ROLE_LIBRARIAN) || roleContainsString(roles, RoleUtil.ROLE_LIBRARIAN_CHECK))
                return "redirect:librarian/home";
            else
                return "login";
        }
        return "login";
    }

    private boolean roleContainsString(Set<Role> roles, String string)
    {
        for (Role role : roles)
        {
            if (string.equals(role.getName()))
                return true;
        }
        return false;
    }

    @RequestMapping(value = "/reader/home", method = RequestMethod.GET)
    public String readerHome()
    {
        return "redirect:reader_condition";
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public String adminHome()
    {
        return "admin/permission_change";
    }

    @RequestMapping(value = "/librarian/home", method = RequestMethod.GET)
    public String librarianHome()
    {
        return "redirect:librarian_homepage";
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

    /*@RequestMapping(value = "/admin/accept", method = RequestMethod.GET)
    @ResponseBody
    public Message accept(int id)
    {
        String role = userService.accept(id);
        if (RoleUtil.ROLE_READER_CHECK.equals(role))
            return new Message("readerHome");
        else if (RoleUtil.ROLE_LIBRARIAN_CHECK.equals(role))
            return new Message("librarianHome");
        return null;
    }*/

    @RequestMapping(value = "/reader/renew", method = RequestMethod.GET)
    @ResponseBody
    public Status renew(int id)
    {
        return new Status(userService.renew(id) ? 1 : 0, globalUtilService.getMaxBorrowDays());
    }

    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public String forgetPassword(String username, Model model)
    {
        System.out.println(username);
        model.addAttribute("status", userService.forgetPassword(username));
        return "forgetPassword";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Status modifyPassword(String password)
    {
        userService.modifyPassword(password);
        return new Status(1);
    }

    @RequestMapping(value = {"/librarian/checkUser", "/reader/checkUser", "/checkUser", "/admin/checkUser"}, method = RequestMethod.GET)
    @ResponseBody
    public Status userExist(String username)
    {
        boolean status = userService.userExist(username);
        return new Status(status ? 0 : 1);
    }

    @RequestMapping(value = "/forget_password", method = RequestMethod.GET)
    public String forget_password()
    {
        return "forgetPassword";
    }

    @RequestMapping(value = "/admin_login", method = RequestMethod.GET)
    public String admin_login()
    {
        return "admin_login";
    }
}
