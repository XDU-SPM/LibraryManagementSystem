package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.config.QueueConfig;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.AccountUtil;
import com.example.library_management_system.utils.BkunitUtil;
import com.example.library_management_system.utils.UserBkunitUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
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

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PunishmentDAO punishmentDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
                return userBkunitDAO.findAllByUserAndStatusBetween(reader, UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW, pageable);
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
                userFavoriteBookDAO.delete(fb);     //判断欲删除的图书是否已被收藏
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int lend(String id, String username)
    {
        User reader = userDAO.findByUsername(username);
        if (reader == null)
            return -1;

        //查询用户是否仍可借图书
        if (reader.getBUL() <= 0)
            return -2;

        // 添加UserBkunit条目
        Optional<Bkunit> optionalBkunit = bkunitDAO.findById(id);
        if (!optionalBkunit.isPresent())
            return -3;

        Bkunit bkunit = optionalBkunit.get();

        UserBkunit userBkunit = null;
        if (bkunit.getStatus() == BkunitUtil.RESERVATION
                && (userBkunit = userBkunitDAO.findByBkunitAndStatus(bkunit, UserBkunitUtil.RESERVATION)) != null
                && userBkunit.getUser().getId() != reader.getId())
            return -4;

        if (userBkunit == null)
            userBkunit = userBkunitDAO.findByUserAndBkunit(reader, bkunit);

        if (userBkunit == null)
            userBkunit = new UserBkunit(new Date(), UserBkunitUtil.BORROWED, bkunit, reader);
        else
        {
            userBkunit.setBorrowDate(new Date());
            userBkunit.setStatus(UserBkunitUtil.BORROWED);
        }

        bkunit.setStatus(BkunitUtil.BORROWED);      // 相对应的Bkunit的状态
        reader.setBUL(reader.getBUL() - 1);   // 修改用户可借图书上限
        userBkunitDAO.save(userBkunit);
        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), reader.getId(), bkunit.getId(), UserBkunitUtil.BORROWED);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);
        return 0;
    }

    public int returnBook(String id, int damage)
    {
        Optional<Bkunit> optionalBkunit = bkunitDAO.findById(id);
        if (!optionalBkunit.isPresent())
            return -1;

        Bkunit bkunit = optionalBkunit.get();
        if (bkunit.getStatus() != BkunitUtil.BORROWED)
            return -2;

        UserBkunit userBkunit = userBkunitDAO.findByBkunitAndStatusBetween(bkunit, UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW);
        User reader = userBkunit.getUser();
        bkunit.setStatus(BkunitUtil.NORMAL);
        reader.setBUL(reader.getBUL() + 1);
        int damageStatus = 0;
        switch (damage)
        {
            case 0:
                damageStatus = BkunitUtil.NO_DAMAGE;
                break;
            case 1:
                damageStatus = BkunitUtil.MILD_DAMAGE;
                break;
            case 2:
                damageStatus = BkunitUtil.MODERATE_DAMAGE;
                break;
            case 3:
                damageStatus = BkunitUtil.MAJOR_DAMAGE;
                break;
            case 4:
                damageStatus = BkunitUtil.LOST;
                bkunit.setStatus(BkunitUtil.LOST);
                break;
        }
        double money = bkunit.getBook().getPrice() * (punishmentDAO.findById(damageStatus).get().getRate() - punishmentDAO.findById(bkunit.getDamageStatus()).get().getRate());
        if (money != 0)
        {
            Account account = new Account(AccountUtil.DAMAGE, money, reader.getId(), new Date());
            accountDAO.save(account);
        }
        reader.deductMoney(money);
        bkunit.setDamageStatus(damageStatus);
        userBkunit.setReturnDate(new Date());
        userBkunit.setStatus(UserBkunitUtil.RETURNED);
        userBkunitDAO.save(userBkunit);
        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), reader.getId(), bkunit.getId(), UserBkunitUtil.RETURNED);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);
        return 0;
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
            reviewDAO.delete(review);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String reserve(String isbn)
    {
        Book book = bookDAO.findByIsbn(isbn);
        Set<Bkunit> bkunits = book.getBkunits();
        Bkunit bkunit = null;
        for (Bkunit bkunit1 : bkunits)
        {
            if (bkunit1.getStatus() == BkunitUtil.NORMAL)
            {
                bkunit = bkunit1;
                break;
            }
        }
        if (bkunit == null)
            return String.valueOf(-1);

        User reader = userService.getUser();
        UserBkunit userBkunit = userBkunitDAO.findByUserAndBkunit(reader, bkunit);
        if (userBkunit == null)
            userBkunit = new UserBkunit(new Date(), UserBkunitUtil.RESERVATION, bkunit, reader);
        else
        {
            userBkunit.setBorrowDate(new Date());
            userBkunit.setStatus(UserBkunitUtil.RESERVATION);
        }

        bkunit.setStatus(BkunitUtil.RESERVATION);
        userBkunitDAO.save(userBkunit);
        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), reader.getId(), bkunit.getId(), UserBkunitUtil.RESERVATION);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);

        int id = userBkunit.getId();
        rabbitTemplate.convertAndSend(QueueConfig.DELAY_QUEUE_PER_MESSAGE_TTL_NAME, id);

        return bkunit.getId();
    }
}
