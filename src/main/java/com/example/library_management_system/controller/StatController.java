package com.example.library_management_system.controller;

import com.example.library_management_system.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatController {

    @Autowired
    private StatService statService;

    //统计reader每月借书情况,统计数据显示在用户个人主页下
     @RequestMapping(path="/user/monthborrow",method={RequestMethod.GET})
    public String monthborrow(Model model, @RequestParam(value="uid") int uid)
     {
         int[] book_month=new int[12];
         statService.monthborrow(book_month,uid);
         model.addAttribute("book_month",book_month);
         return "reader";
     }

   //统计今日借阅多少本书,路径为商定
    @RequestMapping(path="/librarain/borrowbooknum",method={RequestMethod.POST})
    public String borrowbooknum(Model model,@RequestParam(value="uid") int uid)
    {
        int num=statService.borrowbooknum(uid);
        model.addAttribute("borrowbooknum",num);
        return "index";
    }


    //统计今日归还多少本书
    @RequestMapping(path="/librarain/returnbooknum",method={RequestMethod.POST})
    public String returnbooknum(Model model,@RequestParam(value="uid") int uid)
    {
        int returnbooknum=statService.returnbooknum(uid);
        model.addAttribute("returnbooknum",returnbooknum);
        return "index";
    }

   //统计各个书的状态的数量
    @RequestMapping(path="/librarian/statusnum",method={RequestMethod.POST})
    public String statusnum(Model model)
    {
        long[] statusnum= new long[3];
        statusnum=statService.statusnum();
        model.addAttribute("statusnum",statusnum);
        return "index";
    }
}