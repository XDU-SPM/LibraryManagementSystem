package com.example.library_management_system.config;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.service.GlobalUtilService;
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
    private GlobalUtilService globalUtilService;

    /**
     * Execute once every day at 0:00
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void modifyUserBkunitState()
    {
        System.out.println("in modifyUserBkunitState");
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
                System.out.println("in modifyUserBkunitState");
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
    @Scheduled(cron = "0 1 0 * * ?")
    public void deduction()
    {
        System.out.println("in deduction");
        List<User> users = userDAO.findAll();
        double money = globalUtilService.getOverdueMoney();
        for (User user : users)
        {
            List<UserBkunit> userBkunits = userBkunitDAO.findAllByUserAndStatus(user, UserBkunitUtil.OVERDUE);
            for (UserBkunit userBkunit : userBkunits)
            {
                System.out.println("in in deduction");
                Account account = new Account(AccountUtil.OVERDUE, user.getId(), money, userBkunit.getBkunit().getId(), new Date());
                accountDAO.save(account);
            }
            user.addMoney(money * userBkunits.size() * (-1));
            userDAO.save(user);
        }
    }

    @Scheduled(cron = "0 1 0 * * ?")
    public void sendMail()
    {
        System.out.println("in sendMail");
        List<UserBkunit> userBkunits = userBkunitDAO.findAllByStatus(UserBkunitUtil.OVERDUE);
        for (UserBkunit userBkunit : userBkunits)
        {
            System.out.println("in in sendMail");
            User user = userBkunit.getUser();
            Bkunit bkunit = userBkunit.getBkunit();
            String context = user.getUsername() + ", " + bkunit.getBook().getTitle();
            String subject = "233";
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
