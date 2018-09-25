package com.example.library_management_system.config;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.AccountDAO;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.OneDayApart;
import com.example.library_management_system.utils.UserBkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class Timer
{
    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private AccountDAO accountDAO;

    /**
     * Execute once every day at 0:00
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void modifyUserBkunitState()
    {
        List<UserBkunit> userBkunits = userBkunitDAO.findAllByState(UserBkunitUtil.BORROWED);
        Calendar now = Calendar.getInstance();
        for (UserBkunit userBkunit : userBkunits)
        {
            Calendar overdue = Calendar.getInstance();
            overdue.setTime(userBkunit.getDate());
            overdue.add(Calendar.DATE, userBkunit.getDays());
            if (now.getTime().after(overdue.getTime()))
                userBkunit.setState(UserBkunitUtil.OVERDUE);
        }
        userBkunitDAO.saveAll(userBkunits);
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
    public void deduction()
    {
        List<UserBkunit> userBkunits = userBkunitDAO.findAllByState(UserBkunitUtil.OVERDUE);
        OneDayApart oneDayApart = new OneDayApart();
        Account account = accountDAO.findByDateBetweenAndType(oneDayApart.getBefore(), oneDayApart.getAfter(), AccountUtil.OVERDUE);
        if (account == null)
            account = new Account(AccountUtil.OVERDUE, 0, new Date());
        for (UserBkunit userBkunit : userBkunits)
        {
            User user = userBkunit.getUser();
            user.setMoney(user.getMoney() - 1);
            account.addMoney(1);
        }
        userBkunitDAO.saveAll(userBkunits);
        accountDAO.save(account);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendMail()
    {

    }
}
