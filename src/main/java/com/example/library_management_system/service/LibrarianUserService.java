package com.example.library_management_system.service;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.Income;
import com.example.library_management_system.bean.Message;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.controller.LocaleMessageSourceService;
import com.example.library_management_system.dao.AccountDAO;
import com.example.library_management_system.dao.MessageDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.OneDayApart;
import com.example.library_management_system.utils.OneMonthApart;
import com.example.library_management_system.utils.OneWeekApart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class LibrarianUserService
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private MessageDAO messageDAO;

    @Resource
    private LocaleMessageSourceService localeMessageSourceService;

    public int payFine(double money, String username)
    {
        User reader = userDAO.findByUsername(username);

        if (reader == null)
            return 0;

        reader.addMoney(money);
        userDAO.save(reader);

        Account account = new Account(AccountUtil.FINE, reader.getId(), money, null, new Date());
        accountDAO.save(account);

        return 1;
    }

    public void saveReader(User tmp)
    {
        User user = userDAO.findById(tmp.getId());

        user.setUsername(tmp.getUsername());
        user.setName(tmp.getName());
        user.setEmail(tmp.getEmail());
        user.setPassword(tmp.getPassword());

        userDAO.save(user);
    }

    public double todayFineIncome()
    {
        OneDayApart oneDayApart = new OneDayApart();
        Set<Account> accounts = accountDAO.findAllByTypeBetweenAndDateBetween(AccountUtil.DELETE_READER, AccountUtil.DEPOSIT, oneDayApart.getBefore(), oneDayApart.getAfter());
        double money = 0;
        for (Account account : accounts)
        {
            money += account.getMoney();
        }
        return money;
    }

    public Set<Account> getPaidAccounts(int uid)
    {
        Set<Account> accounts = accountDAO.findAllByUidAndType(uid, AccountUtil.FINE);
        for (Account account : accounts)
        {
            setStyle(account);
            setUsername(account);
        }
        return accounts;
    }

    public Set<Account> getUnPaidAccounts(int uid)
    {
        Set<Account> accounts = accountDAO.findAllByUidAndTypeBetween(uid, AccountUtil.OVERDUE, AccountUtil.DAMAGE);
        for (Account account : accounts)
        {
            setStyle(account);
            setUsername(account);
        }
        return accounts;
    }

    private void setUsername(Account account)
    {
        account.setUsername(userDAO.findById(account.getUid()).getUsername());
    }

    private void setStyle(Account account)
    {
        String style = null;
        switch (account.getType())
        {
            case 2:
                style = localeMessageSourceService.getMessage("fineType");
                break;
            case 3:
                style = localeMessageSourceService.getMessage("overdueType");
                break;
            case 4:
                style = localeMessageSourceService.getMessage("damageOrLostType");
                break;
        }
        account.setStyle(style);
    }

    public Collection<Income> getDayIncome()
    {
        Map<Date, Income> map = new TreeMap<>();
        Set<Account> accounts = accountDAO.findAllByTypeBetween(AccountUtil.DELETE_READER, AccountUtil.DEPOSIT);
        for (Account account : accounts)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(account.getDate());
            OneDayApart.getBeginDay(calendar);
            Date date = calendar.getTime();
            if (!map.containsKey(date))
                map.put(date, new Income(date));
            if (account.getType() == AccountUtil.FINE)
                map.get(date).addFine(account.getMoney());
            else
                map.get(date).addDeposit(account.getMoney());
        }
        return map.values();
    }

    public Collection<Income> getWeekIncome()
    {
        Map<Date, Income> map = new TreeMap<>();
        Set<Account> accounts = accountDAO.findAllByTypeBetween(AccountUtil.DELETE_READER, AccountUtil.DEPOSIT);
        for (Account account : accounts)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(account.getDate());
            OneWeekApart.getBeginWeek(calendar);
            Date date = calendar.getTime();
            if (!map.containsKey(date))
                map.put(date, new Income(date));
            if (account.getType() == AccountUtil.FINE)
                map.get(date).addFine(account.getMoney());
            else
                map.get(date).addDeposit(account.getMoney());
        }
        return map.values();
    }

    public Set<Income> getMonthIncome(int year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 0);
        OneMonthApart oneMonthApart = new OneMonthApart(calendar.getTime());
        Set<Income> incomes = new TreeSet<>();
        for (int i = 0; i < 12; i++)
        {
            Income income = new Income(oneMonthApart.getBefore());
            Set<Account> accounts = accountDAO.findAllByTypeBetweenAndDateBetween(AccountUtil.DELETE_READER, AccountUtil.DEPOSIT, oneMonthApart.getBefore(), oneMonthApart.getAfter());
            for (Account account : accounts)
            {
                if (account.getType() == AccountUtil.FINE)
                    income.addFine(account.getMoney());
                else
                    income.addDeposit(account.getMoney());
            }
            incomes.add(income);
            oneMonthApart.setNextMonth();
        }
        return incomes;
    }

    public List<Message> getMessages()
    {
        List<Message> messages = messageDAO.findAll();
        for (Message message : messages)
            setUsername(message);
        return messages;
    }

    private void setUsername(Message message)
    {
        message.setUsername(userDAO.findById(message.getUid()).getUsername());
    }
}
