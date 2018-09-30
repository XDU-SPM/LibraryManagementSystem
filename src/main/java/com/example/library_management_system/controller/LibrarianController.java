package com.example.library_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LibrarianController
{
    @RequestMapping(value = "/librarian/librarian_borrow", method = RequestMethod.GET)
    public String librarian_librarian_borrow()
    {
        return "librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/librarian_return", method = RequestMethod.GET)
    public String librarian_librarian_return()
    {
        return "librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/librarian_table", method = RequestMethod.GET)
    public String librarian_librarian_table()
    {
        return "librarian/librarian_table";
    }

    @RequestMapping(value = "/librarian/librarian_record", method = RequestMethod.GET)
    public String librarian_librarian_record()
    {
        return "librarian/librarian_record";
    }

    @RequestMapping(value = "/librarian/librarian_homepage", method = RequestMethod.GET)
    public String librarian_librarian_homepage()
    {
        return "librarian/librarian_homepage";
    }

    @RequestMapping(value = "/librarian/librarian_user", method = RequestMethod.GET)
    public String librarian_librarian_user()
    {
        return "librarian/librarian_user";
    }
}
