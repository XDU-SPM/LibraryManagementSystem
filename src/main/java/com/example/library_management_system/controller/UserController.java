package com.example.library_management_system.controller;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.Message;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "/login"}, method = {RequestMethod.POST, RequestMethod.GET})
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
                return "redirect:readerHome";
            }
            else
            {
                System.out.println("librarian has logged in");
                return "redirect:librarianHome";
            }
        }
        return "login";
    }

    @RequestMapping(value = "/readerHome", method = RequestMethod.GET)
    @ResponseBody
    public Message readerHome()
    {
        return new Message("reader/home");
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    @ResponseBody
    public Message adminHome()
    {
        return new Message("admin/home");
    }

    @RequestMapping(value = "/librarianHome", method = RequestMethod.GET)
    @ResponseBody
    public Message librarianHome()
    {
        return new Message("librarian/home");
    }

    @RequestMapping(value = "/reader/register", method = RequestMethod.POST)
    @ResponseBody
    public User readerRegister(User student)
    {
        userService.registerService(student, RoleUtil.ROLE_READER_CHECK);
        return student;
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

    @RequestMapping(value = "/accept", method = RequestMethod.GET)
    public String accept(int id)
    {
        String role = userService.accept(id);
        if (RoleUtil.ROLE_READER_CHECK.equals(role))
            return "redirect:/readerHome";
        else if (RoleUtil.ROLE_LIBRARIAN_CHECK.equals(role))
            return "redirect:/librarianHome";
        return null;
    }

    //用户续借图书
    @RequestMapping(value = "/renew",method = RequestMethod.GET)
    public String renew(HttpServletRequest request){
        int id=Integer.parseInt(request.getParameter("id"));
        if(userService.renew(id)){
            return "redirect:/readerquery";
        }
        return null;
    }

    //用户还书
    @RequestMapping(value = "/returnbook",method = RequestMethod.GET)
    public String returnbook(HttpServletRequest request){
        int uid=Integer.parseInt(request.getParameter("username"));
        int buid=Integer.parseInt(request.getParameter("buid"));
        if (userService.returnbook(uid,buid)){
            return "redirect:/readerquery";
        }
        return null;
    }
}
