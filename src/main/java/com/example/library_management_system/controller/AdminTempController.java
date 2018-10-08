package com.example.library_management_system.controller;

import com.example.library_management_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminTempController
{
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "admin/reader_create", method = RequestMethod.GET)
    public String admin_reader_create()
    {
        return "admin/reader_create";
    }

    @RequestMapping(value = "admin/librarian_create", method = RequestMethod.GET)
    public String admin_librarian_create()
    {
        return "admin/librarian_create";
    }

    @RequestMapping(value = "admin/permission_change", method = RequestMethod.GET)
    public String admin_permission_change()
    {
        return "admin/permission_change";
    }

    @RequestMapping(value = "admin/delete_users", method = RequestMethod.GET)
    public String admin_delete_users(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                     String role)
    {
        model.addAttribute("page", adminService.showallinfo(start, size, role));
        return "admin/delete_users";
    }
}
