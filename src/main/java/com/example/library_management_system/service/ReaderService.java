package com.example.library_management_system.service;

import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.UserBkunitDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReaderService
{
    @Autowired
    private UserBkunitDAO userBkunitDAO;

    public UserBkunit appointment(int id)
    {
        return userBkunitDAO.findById(id);
    }
}
