package com.example.library_management_system.controller;

import com.example.library_management_system.bean.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
import com.example.library_management_system.service.AdminBookService;
@Controller
public class AdminBookController {

  @Autowired
    private AdminBookService adminbookservice;

  //前端传来String类型的isbn,调用service层的删除操作,弹窗根据返回的boolean类型的值提示用户
  @RequestMapping(path={"/ManagingBook/deletebook"},method={RequestMethod.POST})
    public boolean  deleteBook(@RequestParam(value = "isbn") String isbn)
  {
       if(adminbookservice.deleteBook()==true)
       {
           return true;
       }
       else
       {
           return false;
       }
  }

//前端传来book对象,调用managingBookService操作，前端根据返回的boolean类型弹窗提示用户
   @RequestMapping(path={"/ManagingBook/addbook"},method={RequestMethod.POST})
    public boolean  addBook(Book book)
   {
       if(adminbookservice.addBook(book)==true)
       {
           return true;
       }
       else
       {
           return false;
       }
   }
   //返回给前端set类型,返回一个页面，分页
   @RequestMapping(path={"/ManagingBook/showbook"},method={RequestMethod.POST})
    public Set<Book>  showBook(Model model)
   {
         model.addAttribute("books",adminbookservice.showBook());
//         return  "bookinfo";
       return null;
   }
}
