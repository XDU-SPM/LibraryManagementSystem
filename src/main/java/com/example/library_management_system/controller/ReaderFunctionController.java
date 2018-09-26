package com.example.library_management_system.controller;

import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.service.ReaderFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ author Captain
 * @ date 2018/9/26
 * @ description as bellow.
 */


@Controller
public class ReaderFunctionController {
    @Autowired
    private ReaderFunctionService readerfunctionservice;

    @RequestMapping(value = "/reader/borrowedBooks",method = RequestMethod.GET)
    @ResponseBody
    public String queryBorrowTools(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                   @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Page<UserBkunit> page = readerfunctionservice.queryborrowedBooks(start, size);
        model.addAttribute("page", page);
        return "queryBorrowedBooks";
    }
}
