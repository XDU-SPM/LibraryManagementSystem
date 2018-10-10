package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.service.BookService;
import com.example.library_management_system.service.LibrarianBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.library_management_system.bean.Bkunit;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import java.util.Set;

@Controller
public class LibrarianBookController
{
    @Autowired
    private LibrarianBookService librarianBookService;

    @Autowired
    private BookService bookService;

    @RequestMapping(path = "/librarian/deletebook", method = RequestMethod.GET)
    public String deleteBkunit(String id, int start)
    {
        librarianBookService.deleteBkunit(id);
        return "redirect:librarian_table?start=" + start;
    }

    @RequestMapping(path = "/librarian/addbook", method = RequestMethod.POST)
    public String addBkunit(Book book, String category, Model model)
    {
        Set<String> ids = librarianBookService.addBkunit(book, category);
        model.addAttribute("ids", ids);
        return "redirect:librarian_table";
    }

    @RequestMapping(path = "/librarian/librarian_table", method = RequestMethod.GET)
    public String showBkunit(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Page<Bkunit> page = librarianBookService.showBkunit(start, size);
        model.addAttribute("page", page);
        return "librarian/librarian_table";
    }

    @RequestMapping(path = "/librarian/librarian_book", method = RequestMethod.GET)
    public String bookInfo(String isbn, Model model)
    {
        Book book = bookService.bookInfo(isbn);
        model.addAttribute("book", book);
        return "/librarian/librarian_book";
    }

    @RequestMapping(value = "/librarian/saveBook", method = RequestMethod.POST)
    public String saveBook(Book book)
    {
        librarianBookService.saveBook(book);
        return "redirect:librarian_book?isbn=" + book.getIsbn();
    }

    @RequestMapping(value = "/librarian/searchBook", method = RequestMethod.GET)
    public String searchBook(String string, int type, Model model,
                             @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", bookService.searchBook(string, type, start, size));
        return "";
    }

    @RequestMapping(value = "/librarian/addBookCategory", method = RequestMethod.POST)
    public String addBookCategory(String isbn, String category)
    {
        librarianBookService.addBookCategory(isbn, category);
        return "";
    }

    @RequestMapping(value = "/librarian/lend", method = RequestMethod.POST)
    public String lend(Model model, String id, String username)
    {
        int status = librarianBookService.lend(id, username);
        model.addAttribute("status", status);
        return "librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/return", method = RequestMethod.POST)
    public String returnBook(Model model, String id, @RequestParam(value = "damage", defaultValue = "0") int damage)
    {
        int status = librarianBookService.returnBook(id, damage);
        model.addAttribute("status", status);
        return "/librarian/librarian_return";
    }

    @RequestMapping(value = "/librarian/librarian_record", method = RequestMethod.GET)
    public String getReserves(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                              @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", librarianBookService.getReserves(start, size));
        return "librarian/librarian_record";
    }

    @RequestMapping(value = "/librarian/librarian_history", method = RequestMethod.GET)
    public String librarian_history()
    {
        return "librarian/librarian_history";
    }
}
