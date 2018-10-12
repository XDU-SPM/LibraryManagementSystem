package com.example.library_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.bean.*;

@Service
public class AdminService
{

    @Autowired
    private GlobalUtilDAO globalUtilDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;

    public void modifyRegisterMoney(double money)
    {
        GlobalUtil globalUtil = globalUtilDAO.findById(1).get();
        globalUtil.setREGISTER_MONEY(money);
        globalUtilDAO.save(globalUtil);
    }

    public void modifyMaxBorrowDays(int days)
    {
        GlobalUtil globalUtil = globalUtilDAO.findById(1).get();
        globalUtil.setMAX_BORROW_DAYS(days);
        globalUtilDAO.save(globalUtil);
    }

    public void modifyMaxBorrowNum(int num)
    {
        GlobalUtil globalUtil = globalUtilDAO.findById(1).get();
        globalUtil.setMAX_BORROW_NUM(num);
        globalUtilDAO.save(globalUtil);
    }

    public void modifyOverdueMoney(double money)
    {
        GlobalUtil globalUtil = globalUtilDAO.findById(1).get();
        globalUtil.setOVERDUE_MONEY(money);
        globalUtilDAO.save(globalUtil);
    }
}
