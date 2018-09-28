package com.example.library_management_system.service;


import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.UserBkunitDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StatService
{
    @Autowired
    private UserBkunitDAO userBkunitDAO;

//    public int[] monthborrow(int[] book_month, int uid)
//    {
//        Set<UserBkunit> record1 = userBkunitDAO.findByUserAndDateBetween();
//        return book_month;
//    }


}
