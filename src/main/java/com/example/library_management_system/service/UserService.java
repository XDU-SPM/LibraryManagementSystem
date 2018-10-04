package com.example.library_management_system.service;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    UserBkunitDAO userBkunitDAO;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    GlobalUtilDAO globalUtilDAO;

    public User getUser()
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findByUsername(userDetails.getUsername());
    }

    public void registerService(User user, String roleName)
    {
        user.setPassword(MD5Util.encode(user.getPassword()));
        if (RoleUtil.ROLE_READER_CHECK.equals(roleName))
        {
            user.setBUL(globalUtilDAO.findById(1).get().getMAX_BORROW_NUM());
            user.setMoney(globalUtilDAO.findById(1).get().getREGISTER_MONEY());
            user.getAccounts().add(new Account(AccountUtil.REGISTER,globalUtilDAO.findById(1).get().getREGISTER_MONEY(), new Date()));
        }
        Role role = roleDAO.findByName(roleName);
        user.getRoles().add(role);
        userDAO.save(user);
    }

    public String accept(int id)
    {
        User user = userDAO.getOne(id);
        if (user.getRoles().contains(roleDAO.findByName(RoleUtil.ROLE_READER_CHECK)))
        {
            user.getRoles().add(roleDAO.findByName(RoleUtil.ROLE_READER));
            userDAO.save(user);
            return RoleUtil.ROLE_READER_CHECK;
        }
        else if (user.getRoles().contains(roleDAO.findByName(RoleUtil.ROLE_LIBRARIAN_CHECK)))
        {
            user.getRoles().add(roleDAO.findByName(RoleUtil.ROLE_LIBRARIAN));
            userDAO.save(user);
            return RoleUtil.ROLE_LIBRARIAN_CHECK;
        }
        return null;
    }

    //续借图书
    public boolean renew(int id)
    {
        UserBkunit userBkunit = userBkunitDAO.findById(id);
        if (userBkunit.getStatus() == UserBkunitUtil.BORROWED)
        {
            //更新续借图书的状态和天数
            userBkunit.setStatus(UserBkunitUtil.RENEW);
            userBkunitDAO.save(userBkunit);
            return true;
        }
        return false;
    }

    //还书
    public boolean returnbook(int uid, int buid)
    {
        User user = userDAO.findById(uid);
        UserBkunit userBkunit = userBkunitDAO.findByUserAndBkunit(null, null);
        //只有在借书、续借、超期状态下才可以还书
        if (userBkunit.getStatus() == UserBkunitUtil.BORROWED || userBkunit.getStatus() == UserBkunitUtil.OVERDUE ||
                userBkunit.getStatus() == UserBkunitUtil.RENEW)
        {
            //判断是否超期
            if (userBkunit.getStatus() == UserBkunitUtil.OVERDUE)
            {
                //需要交的钱
                double money = OverduetimeUtil.getoverduetime(userBkunit.getBorrowDate(), userBkunit.getReturnDate()) * 1;
                user.setMoney(user.getMoney() - money);
                Account account = new Account();
                account.setDate(new Date(System.currentTimeMillis()));
                account.setMoney(money);
                account.setType(AccountUtil.OVERDUE);
                account.setUser(user);
                accountDAO.save(account);
            }
            //可借图书天数加1
            user.setBUL(user.getBUL() + 1);
            userDAO.save(user);
            //修改借书状态
            userBkunit.setStatus(UserBkunitUtil.RETURNED);
            userBkunitDAO.save(userBkunit);
            return true;
        }
        return false;
    }
}
