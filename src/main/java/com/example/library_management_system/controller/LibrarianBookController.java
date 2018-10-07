package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.service.LibrarianBookService;
import com.example.library_management_system.utils.BkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.library_management_system.bean.Bkunit;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class LibrarianBookController
{
    @Autowired
    private LibrarianBookService librarianBookService;

    @RequestMapping(path = {"/librarian/deletebook"}, method = {RequestMethod.GET})
    public String deleteBkunit(@RequestParam(value = "id") String id)
    {
        librarianBookService.deleteBkunit(id);
        return "books";
    }

    @RequestMapping(path = {"/librarian/addbook"}, method = {RequestMethod.POST})
    public String addBkunit(Book book, int number, String category, @RequestParam("file") MultipartFile file)
    {
        librarianBookService.addBkunit(book, number, category, file);
        return "librarian/librarian_table";
    }

    //返回页面booksinfo
    @RequestMapping(path = {"/librarian/librarian_table"}, method = {RequestMethod.GET})
    public String showBkunit(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Page<Bkunit> page = librarianBookService.showbkunit(start, size);
        model.addAttribute("page", page);
        return "librarian/librarian_table";
    }

    @RequestMapping(path = {"/librarian/showbook", "/reader/showbook"}, method = {RequestMethod.GET})
    public String showBook(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                           @RequestParam(value = "size", defaultValue = "10") int size, String category)
    {
        Page<Book> page = librarianBookService.showbook(start, size, category);
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

    @RequestMapping(path = {"/librarian/bookInfo", "/reader/bookInfo"}, method = RequestMethod.GET)
    public String bookInfo(String isbn)
    {
        Book book = librarianBookService.bookInfo(isbn);
        return "";
    }
}
