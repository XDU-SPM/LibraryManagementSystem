package com.example.library_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReaderController
{
    @RequestMapping(value = "/reader/reader_condition", method = RequestMethod.GET)
    public String reader_reader_condition()
    {
        return "reader/reader_condition";
    }

    @RequestMapping(value = "/reader/reader_search", method = RequestMethod.GET)
    public String reader_reader_search()
    {
        return "reader/reader_search";
    }

    @RequestMapping(value = "/reader/reader_information", method = RequestMethod.GET)
    public String reader_reader_information()
    {
        return "reader/reader_information";
    }

    @RequestMapping(value = "/reader/reader_message", method = RequestMethod.GET)
    public String reader_reader_message()
    {
        return "reader/reader_message";
    }

    @RequestMapping(value = "/reader/book_details", method = RequestMethod.GET)
    public String reader_book_details()
    {
        return "reader/book_details";
    }

    @RequestMapping(value = "/reader/reader_comment", method = RequestMethod.GET)
    public String reader_reader_comment()
    {
        return "reader/reader_comment";
    }
}
