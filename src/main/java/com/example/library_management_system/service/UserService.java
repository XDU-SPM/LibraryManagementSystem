package com.example.library_management_system.service;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.RoleDAO;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.MD5Util;
import com.example.library_management_system.utils.RoleUtil;
import com.example.library_management_system.utils.UserBkunitUtil;
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
            user.setMoney(AccountUtil.REGISTER_MONEY);
            user.getAccounts().add(new Account(AccountUtil.REGISTER, AccountUtil.REGISTER_MONEY, new Date()));
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
            return RoleUtil.ROLE_READER_CHECK;
        }
        else if (user.getRoles().contains(roleDAO.findByName(RoleUtil.ROLE_LIBRARIAN_CHECK)))
        {
            user.getRoles().add(roleDAO.findByName(RoleUtil.ROLE_LIBRARIAN));
            return RoleUtil.ROLE_LIBRARIAN_CHECK;
        }
        return null;
    }

    //续借图书
    public boolean renew(int id){
        UserBkunit userBkunit=userBkunitDAO.findById(id);
        if(userBkunit.getState()== UserBkunitUtil.BORROWED){
            //更新续借图书的状态和天数
            userBkunit.setDays(userBkunit.getDays()+30);
            userBkunit.setState(UserBkunitUtil.RENEW);
            userBkunitDAO.save(userBkunit);
            return true;
        }
        return false;
    }
}
