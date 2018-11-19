package com.example.library_management_system.config;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.service.GlobalUtilService;
import com.example.library_management_system.service.LibrarianBookService;
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
import java.text.SimpleDateFormat;
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
    private BkunitOperatingHistoryDAO bkunitOperatingHistoryDAO;

    @Autowired
    private GlobalUtilService globalUtilService;

    @Autowired
    private LibrarianBookService librarianBookService;

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
            User user = userBkunit.getUser();
            Calendar overdue = Calendar.getInstance();
            overdue.setTime(userBkunit.getReturnDate());
            if (now.getTime().after(overdue.getTime()))
            {
                System.out.println("in in modifyUserBkunitState");
                userBkunit.setStatus(UserBkunitUtil.OVERDUE);
                userBkunitDAO.save(userBkunit);

                BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), userBkunit.getUser().getId(), userBkunit.getBkunit().getId(), UserBkunitUtil.OVERDUE);
                bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);
            }
            else if (user.getNotifyDay() > 0)
            {
                Calendar notify = Calendar.getInstance();
                notify.setTime(now.getTime());
                notify.add(Calendar.DATE, user.getNotifyDay());
                if (notify.getTime().after(overdue.getTime()))
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
                    MailUtil.overdueSendMail(userBkunit, false, sdf.format(overdue.getTime()));
                }
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
        librarianBookService.overdueRemindAll();
    }
}
