package com.example.library_management_system.service;

import com.example.library_management_system.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibrarianService
{
    @Autowired
    private UserDAO userDAO;

    public boolean userExist(String username)
    {
        return userDAO.findByUsername(username) != null;
    }
}
