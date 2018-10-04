package com.example.library_management_system.service;


import com.example.library_management_system.bean.Category;
import com.example.library_management_system.bean.MonthBorrow;
import com.example.library_management_system.bean.User;

import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.BkunitDAO;
import com.example.library_management_system.dao.CategoryDAO;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.BkunitUtil;
import com.example.library_management_system.utils.OneMonthApart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class StatService
{
    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BkunitDAO bkunitDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private UserService userService;

    public List<MonthBorrow> monthborrow()
    {
        User reader = userService.getUser();
        OneMonthApart oneMonthApart = new OneMonthApart();
        Stack<MonthBorrow> stack = new Stack<>();
        for (int i = 0; i < 12; i++)
        {
            stack.push(new MonthBorrow(oneMonthApart.getBefore(), userBkunitDAO.countByUserAndBorrowDateBetween(reader, oneMonthApart.getBefore(), oneMonthApart.getAfter())));
            oneMonthApart.setLastMonth();
        }
        List<MonthBorrow> monthBorrows = new ArrayList<>();
        while (!stack.isEmpty())
            monthBorrows.add(stack.pop());
        return monthBorrows;
    }

    public int getUserBUL()
    {
        User reader = userService.getUser();
        return reader.getBUL();
    }

    public int borrowbooknum(int uid)
    {

        Date today2 = new Date();
        Date today1 = new Date();
        today1.setHours(0);
        today1.setMinutes(0);
        today1.setSeconds(0);
        User user = userDAO.findById(uid);
        int number = userBkunitDAO.countByUserAndBorrowDateBetween(user, today1, today2);
        return number;
    }

    public int returnbooknum(int uid)
    {

        Date today2 = new Date();
        Date today1 = new Date();
        today1.setHours(0);
        today1.setMinutes(0);
        today1.setSeconds(0);
        User user = userDAO.findById(uid);
        int number = userBkunitDAO.countByUserAndReturnDateBetween(user, today1, today2);
        return number;
    }


    public long[] statusnum()
    {
        long[] statusnum = new long[3];
        statusnum[0] = bkunitDAO.countByStatus(BkunitUtil.BORROWED);
        statusnum[1] = bkunitDAO.countByStatus(BkunitUtil.NORMAL);
        statusnum[2] = bkunitDAO.countByStatus(BkunitUtil.RESERVATION);
        return statusnum;
    }

    public Map<String, Integer> categorynum()
    {
        Map<String, Integer> map = new TreeMap<>();
        List<UserBkunit> bkunitList = userBkunitDAO.findAllByState(BkunitUtil.BORROWED);
        List<Category> categoryies = categoryDAO.findAll();
        for (Category c : categoryies)
        {
            map.put(c.getName(), 0);
        }
        for (UserBkunit b : bkunitList)
        {
            for (Category i : b.getBkunit().getBook().getCategories())
            {
                String s = i.getName();
                Integer value = map.get(s);
                map.put(s, value++);
            }
        }
        return map;
    }
}
