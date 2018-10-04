package com.example.library_management_system.service;

import com.example.library_management_system.bean.GlobalUtil;
import com.example.library_management_system.dao.GlobalUtilDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ author Captain
 * @ date 2018/10/4
 * @ description as bellow.
 */

@Service
public class GlobalUtilService {

    @Autowired
    private GlobalUtilDAO globalUtilDAO;

    public void initGlobalUtil(){
        GlobalUtil globalUtil = new GlobalUtil();
        globalUtilDAO.save(globalUtil);
    }

    public double getRegisterMoney(){
        return globalUtilDAO.findById(1).get().getREGISTER_MONEY();
    }

    public int getMaxBorrowDays(){
        return globalUtilDAO.findById(1).get().getMAX_BORROW_DAYS();
    }

    public int getMaxBorrowNum(){
        return globalUtilDAO.findById(1).get().getMAX_BORROW_NUM();
    }
}
