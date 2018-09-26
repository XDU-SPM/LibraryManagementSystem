package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.service.LibrarianBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.library_management_system.bean.Bkunit;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

@Controller
public class LibrarianBookController {

  @Autowired
    private LibrarianBookService librarianBookService;

  @RequestMapping(path={"/ManagingBook/deletebook"},method={RequestMethod.POST})
    public boolean  deleteBook(Bkunit bkunit)
  {
      librarianBookService.deleteBook(bkunit);
       return true;
  }

   @RequestMapping(path={"/ManagingBook/addbook"},method={RequestMethod.POST})
    public boolean  addBook(Bkunit bkunit)
   {
       librarianBookService.addBook(bkunit);
       return true;
   }
//返回页面booksinfo
   @RequestMapping(path={"/ManagingBook/showbook"},method={RequestMethod.GET})
    public String showBook(Model model,@RequestParam(value="start",defaultValue="0")int start,@RequestParam(value="size",defaultValue = "10")int size)throws Exception
   {
       Page<Bkunit> page=librarianBookService.showBook(start,size);
       model.addAttribute("page",page);
       return "booksinfo";
   }

   @RequestMapping(path={"/ManagingBook/serchbyid"},method={RequestMethod.GET})
    public Bkunit searchById(@RequestParam(value="id") String id)
   {
          return librarianBookService.searchbyid(id);
   }

   @RequestMapping(path={"/ManagingBook/changeinfo"},method={RequestMethod.POST})
    public boolean changeinfo(Bkunit bkunit)
   {
       return librarianBookService.changeinfo(bkunit);
   }
}
