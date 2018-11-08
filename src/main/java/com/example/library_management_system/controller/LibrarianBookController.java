package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.ReturnMessage;
import com.example.library_management_system.service.BookService;
import com.example.library_management_system.service.LibrarianBookService;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.Status;
import com.example.library_management_system.utils.UserBkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public Status deleteBkunit(String id)
    {
        librarianBookService.deleteBkunit(id);
        return new Status(0);
    }

    @RequestMapping(path = "/librarian/addbook", method = RequestMethod.POST)
    public String addBkunit(Book book, Model model)
    {
        Set<String> ids = librarianBookService.addBkunit(book);
        model.addAttribute("ids", ids);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_barcodes";
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
    public String bookInfo(String id, Model model)
    {
        Book book = bookService.bookInfo1(id);
        model.addAttribute("book", book);
        model.addAttribute("id", id);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "/librarian/librarian_book";
    }

    @RequestMapping(value = "/librarian/saveBook", method = RequestMethod.POST)
    public String saveBook(Book book, Model model, String id)
    {
        librarianBookService.saveBook(book, id);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "redirect:librarian_book?id=" + id;
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

    @RequestMapping(value = "/librarian/removeBookCategory", method = RequestMethod.POST)
    public String removeBookCategory(String isbn, String category, Model model)
    {
        librarianBookService.removeBookCategory(isbn, category);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "";
    }

    @RequestMapping(value = "/librarian/addBookLocation", method = RequestMethod.POST)
    public String addBookLocation(String isbn, String location, Model model)
    {
        librarianBookService.addBookLocation(isbn, location);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "";
    }

    @RequestMapping(value = "/librarian/removeBookLocation", method = RequestMethod.POST)
    public String removeBookLocation(String isbn, String location, Model model)
    {
        librarianBookService.removeBookLocation(isbn, location);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "";
    }

    @RequestMapping(value = "/librarian/lend", method = RequestMethod.POST)
    public String lend(Model model, String id, String number)
    {
        int status = librarianBookService.lend(id, number);
        model.addAttribute("status", status);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_borrow";
    }

    @RequestMapping(value = "/librarian/getReturnMessage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage getReturnMessage(String id, int damage)
    {
        return librarianBookService.getReturnMessage(id, damage);
    }

    @RequestMapping(value = "/librarian/return", method = RequestMethod.POST)
    public String returnBook(Model model, String id, @RequestParam(value = "damage", defaultValue = "0") int damage, double money)
    {
        int status = librarianBookService.returnBook(id, damage, money);
        model.addAttribute("status", status);
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "/librarian/librarian_return";
    }

    @RequestMapping(value = "/librarian/librarian_record", method = RequestMethod.GET)
    public String getReserves(Model model)
    {
        model.addAttribute("set", librarianBookService.getReserves());
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_record";
    }

    @RequestMapping(value = "/librarian/librarian_history", method = RequestMethod.GET)
    public String librarian_history(Model model)
    {
        model.addAttribute("set1", librarianBookService.getBkunitOperatingHistory(UserBkunitUtil.BORROWED));
        model.addAttribute("set2", librarianBookService.getBkunitOperatingHistory(UserBkunitUtil.RETURNED));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_history";
    }

    @RequestMapping(value = "/librarian/librarian_deletebookhistory", method = RequestMethod.GET)
    public String librarian_deletebookhistory(Model model)
    {
        model.addAttribute("set", librarianBookService.getBkunitOperatingHistory(UserBkunitUtil.DELETE));
        model.addAttribute("avatarPath", userService.getUser().getAvatarPath());
        model.addAttribute("username", userService.getUser().getUsername());
        return "librarian/librarian_deletebookhistory";
    }
}
