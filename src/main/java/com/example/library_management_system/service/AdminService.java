package com.example.library_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.bean.*;

import java.util.Set;


@Service
public class AdminService
{
    @Autowired
    private UserDAO userdao;
    @Autowired
    private UserBkunitDAO userbkunitdao;
    @Autowired
    private UserFavoriteBookDAO userfavoritebookdao;
    @Autowired
    private ReviewDAO reviewDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private GlobalUtilDAO globalUtilDAO;

    public void deleteuser(int id)
    {
        User user = userdao.findById(id);
        deleteUserBkunits(user);
        deleteReviews(user);
        deleteUserFavoriteBooks(user);
        userdao.deleteById(id);
    }

    private void deleteUserBkunits(User user)
    {
        Set<UserBkunit> userBkunits = user.getUserBkunits();
        for (UserBkunit userBkunit : userBkunits)
        {
            userBkunit.setUser(null);
            userBkunit.setBkunit(null);
        }
        userbkunitdao.saveAll(userBkunits);
    }

    private void deleteReviews(User user)
    {
        Set<Review> reviews = user.getReviews();
        for (Review review : reviews)
        {
            review.setUser(null);
            review.setBook(null);
        }
        reviewDAO.saveAll(reviews);
    }

    private void deleteUserFavoriteBooks(User user)
    {
        Set<UserFavoriteBook> userFavoriteBooks = user.getUserFavoriteBooks();
        for (UserFavoriteBook userFavoriteBook : userFavoriteBooks)
        {
            userFavoriteBook.setUser(null);
            userFavoriteBook.setBook(null);
        }
        userfavoritebookdao.saveAll(userFavoriteBooks);
    }

    public Page<User> showallinfo(int start, int size, String role)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Role r = roleDAO.findByName(role);
        return userdao.findByRolesContaining(r, pageable);
    }

    public User showinfo(int id)
    {
        return userdao.findById(id);
    }

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
        globalUtil.setREGISTER_MONEY(num);
        globalUtilDAO.save(globalUtil);
    }
}
