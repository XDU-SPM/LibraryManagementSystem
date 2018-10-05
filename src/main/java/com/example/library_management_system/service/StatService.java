package com.example.library_management_system.service;


import com.example.library_management_system.bean.*;

import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.BkunitUtil;
import com.example.library_management_system.utils.OneDayApart;
import com.example.library_management_system.utils.OneMonthApart;
import com.example.library_management_system.utils.UserBkunitUtil;
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

    @Autowired
    private BkunitOperatingHistoryDAO bkunitOperatingHistoryDAO;

    @Autowired
    private AccountDAO accountDAO;

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

    public long todayBorrowNumber()
    {
        OneDayApart oneDayApart = new OneDayApart();
        return bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(oneDayApart.getBefore(), oneDayApart.getAfter(), UserBkunitUtil.BORROWED);
    }

    public long todayReturnNumber()
    {
        OneDayApart oneDayApart = new OneDayApart();
        return bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(oneDayApart.getBefore(), oneDayApart.getAfter(), UserBkunitUtil.RETURNED);
    }

    public long todayOverdueNumber()
    {
        OneDayApart oneDayApart = new OneDayApart();
        return bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(oneDayApart.getBefore(), oneDayApart.getAfter(), UserBkunitUtil.OVERDUE);
    }

    public double todayFineIncome()
    {
        OneDayApart oneDayApart = new OneDayApart();
        Set<Account> accounts = accountDAO.findAllByDateBetween(oneDayApart.getBefore(), oneDayApart.getAfter());
        double money = 0;
        for (Account account : accounts)
        {
            money += account.getMoney();
        }
        return money;
    }

    public long[] statusnum()
    {
        long[] statusnum = new long[3];
        statusnum[0] = bkunitDAO.countByStatus(BkunitUtil.BORROWED);
        statusnum[1] = bkunitDAO.countByStatus(BkunitUtil.NORMAL);
        statusnum[2] = bkunitDAO.countByStatus(BkunitUtil.RESERVATION);
        return statusnum;
    }

    public List<DayBorrowReturn> dayBorrowReturns()
    {
        List<DayBorrowReturn> dayBorrowReturns = new ArrayList<>();
        OneDayApart oneDayApart = new OneDayApart();
        Stack<DayBorrowReturn> stack = new Stack<>();
        for (int i = 0; i < 7; i++)
        {
            Date before = oneDayApart.getBefore();
            Date after = oneDayApart.getAfter();
            stack.push(new DayBorrowReturn(new Date(),
                    bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(before, after, UserBkunitUtil.BORROWED),
                    bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(before, after, UserBkunitUtil.RETURNED)));
            oneDayApart.setLastDay();
        }
        while (!stack.isEmpty())
            dayBorrowReturns.add(stack.pop());
        return dayBorrowReturns;
    }

    public Map<String, Integer> categorynum()
    {
        Map<String, Integer> map = new TreeMap<>();
        List<UserBkunit> bkunitList = userBkunitDAO.findAllByStatus(BkunitUtil.BORROWED);
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
