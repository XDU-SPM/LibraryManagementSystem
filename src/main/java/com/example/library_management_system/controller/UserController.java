package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.BookService;
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
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Set;

@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @RequestMapping(value = {"/", "/visitor-main"}, method = RequestMethod.GET)
    public String main(Model model)
    {
        model.addAttribute("page", bookService.showBook(0, 12, null));
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
        return "redirect:permission_change";
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

    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public String forgetPassword(String username, Model model)
    {
        System.out.println(username);
        model.addAttribute("status", userService.forgetPassword(username));
        return "forgetPassword";
    }

    @RequestMapping(value = {"/librarian/checkUser", "/reader/checkUser", "/checkUser", "/admin/checkUser"}, method = RequestMethod.GET)
    @ResponseBody
    public Status userExist(String username, int id)
    {
        boolean status = userService.userExist(username, id);
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

    @RequestMapping(value = "/librarian/deleteReader", method = RequestMethod.GET)
    @ResponseBody
    public Status deleteReader(int id)
    {
        return new Status(userService.deleteUser(id));
    }

    @RequestMapping(value = {"/changeSessionLanguage", "/admin/changeSessionLanguage", "/reader/changeSessionLanguage", "/librarian/changeSessionLanguage"},
            method = RequestMethod.GET)
    public String changeSessionLanguage(String lang, HttpServletRequest request)
    {
        String url = request.getHeader("referer");
        if ("zh".equals(lang))
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("zh", "CN"));
        else if ("en".equals(lang))
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("en", "US"));
        return "redirect:" + url;
    }

    @RequestMapping(value = {"/changeSessionLanguage1", "/admin/changeSessionLanguage1", "/reader/changeSessionLanguage1", "/librarian/changeSessionLanguage1"},
            method = RequestMethod.GET)
    public String changeSessionLanguage(String lang, HttpServletRequest request, String page)
    {
        if ("zh".equals(lang))
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("zh", "CN"));
        else if ("en".equals(lang))
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("en", "US"));
        return "redirect:" + page;
    }
}
