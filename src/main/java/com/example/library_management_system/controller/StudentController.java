package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Student;
import com.example.library_management_system.service.StudentService;
import com.example.library_management_system.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StudentController
{
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = {"/student/", "/student/login"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String login()
    {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
        {
            System.out.println("student/home");
            return "redirect:/student/home";
        }
        System.out.println("student/login");
        return "student/login";
    }

    @RequestMapping(value = "/student/home", method = RequestMethod.GET)
    @ResponseBody
    public Message home()
    {
        return new Message("student/home");
    }

    @RequestMapping(value = "/student/register", method = RequestMethod.POST)
    @ResponseBody
    public Student register(Student student)
    {
        studentService.registerService(student);
        return student;
    }
}
