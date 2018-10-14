package com.example.library_management_system.service;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.Income;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.dao.AccountDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.OneDayApart;
import com.example.library_management_system.utils.OneMonthApart;
import com.example.library_management_system.utils.OneWeekApart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LibrarianUserService
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AccountDAO accountDAO;

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

    // TODO: 2018/10/10 bug
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
                style = "fine";
                break;
            case 3:
                style = "overdue";
                break;
            case 4:
                style = "damage or lost";
                break;
        }
        account.setStyle(style);
    }

    public Collection<Income> getDayIncome()
    {
        Map<Date, Income> map = new TreeMap<>();
        Set<Account> accounts = accountDAO.findAllByTypeOrType(AccountUtil.FINE, AccountUtil.DEPOSIT);
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
        Set<Account> accounts = accountDAO.findAllByTypeOrType(AccountUtil.FINE, AccountUtil.DEPOSIT);
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
            Set<Account> accounts = accountDAO.findAllByTypeBetweenAndDateBetween(AccountUtil.FINE, AccountUtil.DEPOSIT, oneMonthApart.getBefore(), oneMonthApart.getAfter());
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
}
