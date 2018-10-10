package com.example.library_management_system.service;

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
}
