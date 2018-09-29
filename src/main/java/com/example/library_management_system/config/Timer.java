package com.example.library_management_system.config;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.AccountDAO;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.GlobalUtil;
import com.example.library_management_system.utils.MailUtil;
import com.example.library_management_system.utils.UserBkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class Timer
{
    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserDAO userDAO;

    /**
     * Execute once every day at 0:00
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void modifyUserBkunitState()
    {
        List<UserBkunit> userBkunits = userBkunitDAO.findAllByStateOrState(UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW);
        Calendar now = Calendar.getInstance();
        for (UserBkunit userBkunit : userBkunits)
        {
            Calendar overdue = Calendar.getInstance();
            overdue.setTime(userBkunit.getBorrowDate());
            int days;
            if (userBkunit.getState() == UserBkunitUtil.BORROWED)
                days = GlobalUtil.MAX_BORROW_DAYS;
            else
                days = GlobalUtil.MAX_BORROW_DAYS * 2;
            overdue.add(Calendar.DATE, days);
            if (now.getTime().after(overdue.getTime()))
                userBkunit.setState(UserBkunitUtil.OVERDUE);
        }
        userBkunitDAO.saveAll(userBkunits);
    }

    /**
     * How to test:
     * Two books borrowed by one reader have expired.
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
    public void deduction()
    {
        List<User> users = userDAO.findAll();
        for (User user : users)
        {
            int num = 0;
            Set<UserBkunit> userBkunits = user.getUserBkunits();
            for (UserBkunit userBkunit : userBkunits)
            {
                if (userBkunit.getState() == UserBkunitUtil.OVERDUE)
                    num++;
            }
            Account account = new Account(AccountUtil.OVERDUE, num, new Date());
            user.deductMoney(num);
            user.getAccounts().add(account);
            userDAO.save(user);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendMail()
    {
        List<UserBkunit> userBkunits = userBkunitDAO.findAllByState(UserBkunitUtil.OVERDUE);
        for (UserBkunit userBkunit : userBkunits)
        {
            User user = userBkunit.getUser();
            String context = "";
            String subject = "";
            try
            {
                MailUtil.sendmail(user.getEmail(), context, subject);
            }
            catch (MessagingException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
    }
}
