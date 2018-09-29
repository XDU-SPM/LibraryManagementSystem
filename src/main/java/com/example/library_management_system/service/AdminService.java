package com.example.library_management_system.service;

import com.example.library_management_system.utils.GlobalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.bean.*;


@Service
public class AdminService
{

    @Autowired
    private UserDAO userdao;
    @Autowired
    private UserBkunitDAO userbkunitdao;
    @Autowired
    private UserFavoriteBookDAO userfavoritebookdao;

    public void deleteuser(int id)
    {
        //删除主表数据，也会删除从表数据
        userdao.deleteById(id);
    }

    public Page<User> showallinfo(int start, int size,String role)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Role r=new Role(role);
        Page<User> page = userdao.findByRolesContaining(r,pageable);
        return page;
    }

    public User showinfo(int id)
    {
        return userdao.findById(id);
    }

    public void modifyRegisterMoney(double money)
    {
        GlobalUtil.REGISTER_MONEY = money;
    }

    public void modifyMaxBorrowDays(int days)
    {
        GlobalUtil.MAX_BORROW_DAYS = days;
    }

    public void modifyMaxBorrowNum(int num)
    {
        GlobalUtil.MAX_BORROW_NUM = num;
    }
}
