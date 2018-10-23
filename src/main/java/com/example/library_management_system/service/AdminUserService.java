package com.example.library_management_system.service;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService
{
    @Autowired
    private UserDAO userDAO;

    public String getLibrarianPassword(int id)
    {
        return userDAO.findById(id).getPassword();
    }

    public void saveLibrarian(User tmp)
    {
        User user = userDAO.findById(tmp.getId());

        user.setUsername(tmp.getUsername());
        user.setName(tmp.getName());
        user.setEmail(tmp.getEmail());

        userDAO.save(user);
    }

    public String findPassword(String username)
    {
        User user = userDAO.findByUsername(username);
        return user.getPassword();
    }
}
