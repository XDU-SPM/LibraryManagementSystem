package com.example.library_management_system.service;

import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.UserBkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LibrarianService
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserBkunitDAO userBkunitDAO;

    public boolean userExist(String username)
    {
        return userDAO.findByUsername(username) != null;
    }

    public Page<UserBkunit> getReserves(int start, int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "borrowDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        return userBkunitDAO.findAllByStatusBetween(UserBkunitUtil.RESERVATION, UserBkunitUtil.RESERVATION_CANCEL, pageable);
    }
}
