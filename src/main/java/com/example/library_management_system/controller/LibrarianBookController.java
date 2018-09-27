package com.example.library_management_system.controller;

import com.example.library_management_system.service.LibrarianBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.library_management_system.bean.Bkunit;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LibrarianBookController
{
    @Autowired
    private LibrarianBookService librarianBookService;

    @RequestMapping(path = {"/librarian/deletebook"}, method = {RequestMethod.POST})
    public String deleteBkunit(Bkunit bkunit)
    {
        librarianBookService.deleteBkunit(bkunit);
        return "books";
    }

    @RequestMapping(path = {"/librarian/addbook"}, method = {RequestMethod.POST})
    @ResponseBody
    public Bkunit addBkunit(Bkunit bkunit)
    {
//        bkunit.setId(String.valueOf(System.currentTimeMillis()));
//        if(!librarianBookService.isexist(bkunit.getBook()))
//        {
//            librarianBookService.addBook(bkunit.getBook());
//        }
//        librarianBookService.addBkunit(bkunit);
        return bkunit;
    }

    //返回页面booksinfo
    @RequestMapping(path = {"/librarian/showbook"}, method = {RequestMethod.GET})
    public String showBook(Model model, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) throws Exception
    {
        Page<Bkunit> page = librarianBookService.showBook(start, size);
        model.addAttribute("page", page);
        return "booksinfo";
    }

    @RequestMapping(path = {"/librarian/serchbyid"}, method = {RequestMethod.GET})
    public Bkunit searchById(@RequestParam(value = "id") String id)
    {
        return librarianBookService.searchbyid(id);
    }

    @RequestMapping(path = {"/librarian/changeinfo"}, method = {RequestMethod.POST})
    public String changeinfo(Bkunit bkunit)
    {
        librarianBookService.changeinfo(bkunit);
        return "booksinfo";
    }

}
