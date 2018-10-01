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

    public void deleteuser(int id)
    {
        User user = userdao.findById(id);
        deleteUserBkunits(user);
        deleteReviews(user);
        deleteUserFavoriteBooks(user);
        deleteAccounts(user);
        userdao.deleteById(id);
    }

    private void deleteUserBkunits(User user)
    {
        Set<UserBkunit> userBkunits = user.getUserBkunits();
        if (userBkunits != null)
        {
            for (UserBkunit userBkunit : userBkunits)
            {
                userBkunit.setUser(null);
                userBkunit.setBkunit(null);
            }
            userbkunitdao.saveAll(userBkunits);
        }
    }

    private void deleteReviews(User user)
    {
        Set<Review> reviews = user.getReviews();
        if (reviews != null)
        {
            for (Review review : reviews)
            {
                review.setUser(null);
                review.setBook(null);
            }
            reviewDAO.saveAll(reviews);
        }
    }

    private void deleteUserFavoriteBooks(User user)
    {
        Set<UserFavoriteBook> userFavoriteBooks = user.getUserFavoriteBooks();
        if (userFavoriteBooks != null)
        {
            for (UserFavoriteBook userFavoriteBook : userFavoriteBooks)
            {
                userFavoriteBook.setUser(null);
                userFavoriteBook.setBook(null);
            }
            userfavoritebookdao.saveAll(userFavoriteBooks);
        }
    }

    private void deleteAccounts(User user)
    {
        Set<Account> accounts = user.getAccounts();
        if (accounts != null)
        {
            for (Account account : accounts)
            {
                account.setUser(null);
            }
            accountDAO.saveAll(accounts);
        }
    }

    public Page<User> showallinfo(int start, int size, String role)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Role r = new Role(role);
        Page<User> page = userdao.findByRolesContaining(r, pageable);
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
