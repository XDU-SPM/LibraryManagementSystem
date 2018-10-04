package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.BkunitUtil;
import com.example.library_management_system.utils.UserBkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @ author Captain
 * @ date 2018/9/26
 * @ description as bellow.
 */


@Service
public class ReaderFunctionService
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private UserFavoriteBookDAO userFavoriteBookDAO;

    @Autowired
    private BkunitDAO bkunitDAO;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private BkunitOperatingHistoryDAO bkunitOperatingHistoryDAO;

    public Page<UserBkunit> queryborrowedBooks(int start, int size, int status)
    {
        User reader = userService.getUser();
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.ASC, "borrowDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        switch (status)
        {
            case 1:     // 预约
                return userBkunitDAO.findAllByUserAndStatus(reader, UserBkunitUtil.RESERVATION, pageable);
            case 2:     // 未还
                return userBkunitDAO.findAllByUserAndStatusOrStatusOrStatus(reader, UserBkunitUtil.OVERDUE, UserBkunitUtil.RENEW, UserBkunitUtil.BORROWED, pageable);
            case 3:     // 已还
                return userBkunitDAO.findAllByUserAndStatus(reader, UserBkunitUtil.RETURNED, pageable);
        }
        return null;
    }

    public Page<UserFavoriteBook> queryFavoriteBooks(int start, int size)
    {
        User reader = userService.getUser();
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.ASC, "borrowDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        return userFavoriteBookDAO.findAllByUser(reader, pageable);
    }


    public boolean addFavoriteBook(String Isbn)
    {
        try
        {
            Book book = bookDAO.findByIsbn(Isbn);
            User reader = userService.getUser();
            UserFavoriteBook fb = userFavoriteBookDAO.findByUserAndBook(reader, book);
            if (fb == null) userFavoriteBookDAO.save(fb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteFavoriteBook(String Isbn)
    {
        try
        {
            Book book = bookDAO.findByIsbn(Isbn);
            User reader = userService.getUser();
            UserFavoriteBook fb = userFavoriteBookDAO.findByUserAndBook(reader, book);
            if (fb != null)
            {
                fb.setBook(null);
                fb.setUser(null);
                userFavoriteBookDAO.save(fb);     //判断欲删除的图书是否已被收藏
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public int lend(String bookIsbn)
    {
        User reader = userService.getUser();
        // 判断传入的参数是否合法
        Book bk = bookDAO.findByIsbn(bookIsbn);
        if (bk == null) return 0;

        // 判断图书状态，是否可借(除了预订中的书籍，图书数目是否还有余量)
        long num = bkunitDAO.countAllByBookAndStatus(bk, BkunitUtil.NORMAL);   // 统计该书在馆且未被预约的数量
        if (num <= 0) return 0;

        //查询用户是否仍可借图书
        if (reader.getBUL() <= 0) return 0;

        // 添加UserBkunit条目
        Set<Bkunit> bkunits = bkunitDAO.findAllByBookAndStatus(bk, BkunitUtil.NORMAL);
        Iterator<Bkunit> iterator = bkunits.iterator();
        Bkunit bkunit = iterator.next();
        bkunit.setStatus(BkunitUtil.BORROWED);      // 相对应的Bkunit的状态
        reader.setBUL(reader.getBUL() - 1);   // 修改用户可借图书上限
        UserBkunit userBkunit = new UserBkunit(new Date(), BkunitUtil.BORROWED, bkunit, reader);
        userBkunitDAO.save(userBkunit);
        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), reader.getId(), bkunit.getId(), BkunitUtil.BORROWED);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);
        return 1;
    }


    public int writeReview(String Isbn, String review)
    {
        try
        {
            Book bk = bookDAO.findByIsbn(Isbn);
            Review rv = new Review(review, new Date(), bk, userService.getUser());
            reviewDAO.save(rv);
        }
        catch (Exception e)
        {
            return 0;
        }
        return 1;
    }

    public Page<Review> bookReview(int start, int size, String Isbn)
    {
        Book book = bookDAO.findByIsbn(Isbn);
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.ASC, "borrowDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        return reviewDAO.findAllByBook(book, pageable);
    }


    public boolean deleteReview(int rid)
    {
        try
        {
            Review review = reviewDAO.findById(rid);
            review.setBook(null);
            review.setUser(null);
            reviewDAO.save(review);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
