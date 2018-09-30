package com.example.library_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminTempController
{
    @RequestMapping(value = "admin/admin-index", method = RequestMethod.GET)
    public String admin_admin_index()
    {
        return "admin/admin-index";
    }

    @RequestMapping(value = "admin/admin-user", method = RequestMethod.GET)
    public String admin_admin_user()
    {
        return "admin/admin-user";
    }

    @RequestMapping(value = "admin/admin-help", method = RequestMethod.GET)
    public String admin_admin_help()
    {
        return "admin/admin-help";
    }

    @RequestMapping(value = "admin/admin-gallery", method = RequestMethod.GET)
    public String admin_admin_gallery()
    {
        return "admin/admin-gallery";
    }

    @RequestMapping(value = "admin/admin-log", method = RequestMethod.GET)
    public String admin_admin_log()
    {
        return "admin/admin-log";
    }

    @RequestMapping(value = "admin/admin-form", method = RequestMethod.GET)
    public String admin_admin_form()
    {
        return "admin/admin-form";
    }

    @RequestMapping(value = "admin/admin-table", method = RequestMethod.GET)
    public String admin_admin_table()
    {
        return "admin/admin-table";
    }

    @RequestMapping(value = "admin/admin-404", method = RequestMethod.GET)
    public String admin_admin_404()
    {
        return "admin/admin-404";
    }
}
