package com.example.library_management_system.service;

import com.example.library_management_system.bean.Account;
import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.dao.RoleDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.MD5Util;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserService
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    public User getUser()
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findByUsername(userDetails.getUsername());
    }

    public void registerService(User user, String roleName)
    {
        user.setPassword(MD5Util.encode(user.getPassword()));
        user.setMoney(AccountUtil.REGISTER_MONEY);
        user.getAccounts().add(new Account(AccountUtil.REGISTER, AccountUtil.REGISTER_MONEY, new Date()));
        Role role = roleDAO.findByName(roleName);
        user.getRoles().add(role);
        userDAO.save(user);
    }

    public String accept()
    {
        User user = getUser();
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
}
