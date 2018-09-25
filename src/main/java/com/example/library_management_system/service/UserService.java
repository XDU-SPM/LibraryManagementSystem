package com.example.library_management_system.service;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.dao.RoleDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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
        user.setMoney(300);
        Role role = roleDAO.findByName(roleName);
        if (user.getRoles() == null)
            user.setRoles(new HashSet<>());
        user.getRoles().add(role);
        userDAO.save(user);
    }
}
