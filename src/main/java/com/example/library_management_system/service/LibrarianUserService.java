package com.example.library_management_system.service;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.dao.AccountDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.OneDayApart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

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
        Set<Account> accounts =  accountDAO.findAllByUidAndType(uid, AccountUtil.FINE);
        for (Account account : accounts)
        {
            setStyle(account);
            setUsername(account);
        }
        return accounts;
    }

    public Set<Account> getUnPaidAccounts(int uid)
    {
        Set<Account> accounts =  accountDAO.findAllByUidAndTypeBetween(uid, AccountUtil.OVERDUE, AccountUtil.DAMAGE);
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
}
