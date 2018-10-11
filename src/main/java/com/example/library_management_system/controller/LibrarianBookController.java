package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.service.BookService;
import com.example.library_management_system.service.LibrarianBookService;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.UserBkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class LibrarianBookController
{
    @Autowired
    private LibrarianBookService librarianBookService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/librarian/deletebook", method = RequestMethod.GET)
    public String deleteBkunit(String id, int start, Model model)
    {
        librarianBookService.deleteBkunit(id);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:librarian_table?start=" + start;
    }

    @RequestMapping(path = "/librarian/addbook", method = RequestMethod.POST)
    public String addBkunit(Book book, String category, Model model)
    {
        Set<String> ids = librarianBookService.addBkunit(book, category);
        model.addAttribute("ids", ids);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:librarian_table";
    }

    @RequestMapping(path = "/librarian/librarian_table", method = RequestMethod.GET)
    public String showBkunit(Model model)
    {
        model.addAttribute("set", librarianBookService.showBkunit());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_table";
    }

    @RequestMapping(path = "/librarian/librarian_book", method = RequestMethod.GET)
    public String bookInfo(String isbn, Model model)
    {
        Book book = bookService.bookInfo(isbn);
        model.addAttribute("book", book);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "/librarian/librarian_book";
    }

    @RequestMapping(value = "/librarian/saveBook", method = RequestMethod.POST)
    public String saveBook(Book book, Model model)
    {
        librarianBookService.saveBook(book);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:librarian_book?isbn=" + book.getIsbn();
    }

    @RequestMapping(value = "/librarian/searchBook", method = RequestMethod.GET)
    public String searchBook(String string, Model model,
                             @RequestParam(value = "start", defaultValue = "0") int start,
                             @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", bookService.searchBook(string, start, size));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "";
    }

    @RequestMapping(value = "/librarian/addBookCategory", method = RequestMethod.POST)
    public String addBookCategory(String isbn, String category, Model model)
    {
        librarianBookService.addBookCategory(isbn, category);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "";
    }

    @RequestMapping(value = "/librarian/lend", method = RequestMethod.POST)
    public String lend(Model model, String id, String username)
    {
        int status = librarianBookService.lend(id, username);
        model.addAttribute("status", status);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/return", method = RequestMethod.POST)
    public String returnBook(Model model, String id, @RequestParam(value = "damage", defaultValue = "0") int damage)
    {
        int status = librarianBookService.returnBook(id, damage);
        model.addAttribute("status", status);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "/librarian/librarian_return";
    }

    @RequestMapping(value = "/librarian/librarian_record", method = RequestMethod.GET)
    public String getReserves(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                              @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", librarianBookService.getReserves(start, size));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_record";
    }

    @RequestMapping(value = "/librarian/librarian_history", method = RequestMethod.GET)
    public String librarian_history(Model model)
    {
        model.addAttribute("set1", librarianBookService.getBkunitOperatingHistory(UserBkunitUtil.BORROWED, true));
        model.addAttribute("set2", librarianBookService.getBkunitOperatingHistory(UserBkunitUtil.RETURNED, true));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_history";
    }

    @RequestMapping(value = "/librarian/librarian_deletebookhistory", method = RequestMethod.GET)
    public String librarian_deletebookhistory(Model model, @RequestParam(value = "start", defaultValue = "0") int start,
                                              @RequestParam(value = "size", defaultValue = "10") int size)
    {
        model.addAttribute("page", librarianBookService.getBkunitOperatingHistory(start, size, UserBkunitUtil.DELETE, true));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_deletebookhistory";
    }
}
