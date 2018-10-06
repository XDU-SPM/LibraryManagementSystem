package com.example.library_management_system.config;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.BkunitOperatingHistory;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.AccountUtil;
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

    @Autowired
    private GlobalUtilDAO globalUtilDAO;

    @Autowired
    private BkunitOperatingHistoryDAO bkunitOperatingHistoryDAO;

    @Autowired
    private GlobalUtilDAO getGlobalUtilDAO;

    /**
     * Execute once every day at 0:00
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void modifyUserBkunitState()
    {
        List<UserBkunit> userBkunits = userBkunitDAO.findAllByStatusOrStatus(UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW);
        Calendar now = Calendar.getInstance();
        for (UserBkunit userBkunit : userBkunits)
        {
            Calendar overdue = Calendar.getInstance();
            overdue.setTime(userBkunit.getBorrowDate());
            int days;
            if (userBkunit.getStatus() == UserBkunitUtil.BORROWED)
                days =  globalUtilDAO.findById(1).get().getMAX_BORROW_DAYS();
            else
                days = globalUtilDAO.findById(1).get().getMAX_BORROW_DAYS() * 2;
            overdue.add(Calendar.DATE, days);
            if (now.getTime().after(overdue.getTime()))
            {
                userBkunit.setStatus(UserBkunitUtil.OVERDUE);
                userBkunitDAO.save(userBkunit);
                BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), userBkunit.getUser().getId(), userBkunit.getBkunit().getId(), UserBkunitUtil.OVERDUE);
                bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);
            }
        }
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
            double num = userBkunitDAO.countByUserAndStatus(user, UserBkunitUtil.OVERDUE) * getGlobalUtilDAO.findById(1).get().getOVERDUE_MONEY();
            Account account = new Account(AccountUtil.OVERDUE, num, user.getId(), new Date());
            accountDAO.save(account);
            user.deductMoney(num);
            userDAO.save(user);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendMail()
    {
        List<UserBkunit> userBkunits = userBkunitDAO.findAllByStatus(UserBkunitUtil.OVERDUE);
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
