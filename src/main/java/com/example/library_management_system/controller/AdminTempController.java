package com.example.library_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminTempController
{
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
}
