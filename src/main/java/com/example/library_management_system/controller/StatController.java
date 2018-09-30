package com.example.library_management_system.controller;

import com.example.library_management_system.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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

    //有问题，UserBkunit是不是借阅历史？？？？？？？？？？？
    @RequestMapping(path="/categorynum",method={RequestMethod.POST})
    public String categorynum(Model model)
    {
        Map<String,Integer> map=statService.categorynum();
        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });
        Map<String,Integer> map1=new HashMap<>();
        int count=0;
        for(Map.Entry<String,Integer> entry : map.entrySet())
        {

            String s=entry.getKey();
            Integer i=entry.getValue();
            map1.put(s,i);
            if(count==7)
            {
                break;
            }
            count++;
        }
           model.addAttribute("map1",map1);
           return "index";
    }
}
