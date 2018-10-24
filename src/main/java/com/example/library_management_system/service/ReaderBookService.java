package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.config.QueueConfig;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReaderBookService
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private ReturnHistoryDAO returnHistoryDAO;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BkunitOperatingHistoryDAO bkunitOperatingHistoryDAO;

    @Autowired
    private UserBookDAO userBookDAO;

    @Autowired
    private UserFavoriteBookDAO userFavoriteBookDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private GlobalUtilService globalUtilService;

    public List<MonthBorrow> monthBorrows()
    {
        User reader = userService.getUser();
        OneMonthApart oneMonthApart = new OneMonthApart();
        Stack<MonthBorrow> stack = new Stack<>();
        for (int i = 0; i < 12; i++)
        {
            stack.push(new MonthBorrow(oneMonthApart.getBefore(),
                    userBkunitDAO.countByUserAndBorrowDateBetween(reader, oneMonthApart.getBefore(), oneMonthApart.getAfter())));
            oneMonthApart.setLastMonth();
        }
        List<MonthBorrow> monthBorrows = new ArrayList<>();
        while (!stack.isEmpty())
            monthBorrows.add(stack.pop());
        return monthBorrows;
    }

    public Page<UserBkunit> queryBorrowedBooks(int start, int size)
    {
        User reader = userService.getUser();
        Pageable pageable = PageableUtil.pageable(false, "borrowDate", start, size);
        return userBkunitDAO.findAllByUserAndStatusBetween(reader, UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW, pageable);
    }

    public Page<UserBook> queryReservedBooks(int start, int size)
    {
        User reader = userService.getUser();
        Pageable pageable = PageableUtil.pageable(false, "borrowDate", start, size);
        return userBookDAO.findAllByUserAndStatus(reader, UserBookUtil.RESERVATION, pageable);
    }

    public Page<ReturnHistory> queryReturnedBooks(int start, int size)
    {
        User reader = userService.getUser();
        Pageable pageable = PageableUtil.pageable(false, "borrowDate", start, size);
        return returnHistoryDAO.findAllByUserId(reader.getId(), pageable);
    }

    /**
     * 不会出现没有书的情况
     *
     * @param isbn
     * @return
     */
    public void reserve(String isbn)
    {
        Book book = bookDAO.findByIsbn(isbn);
        book.addNumber(-1);
        User reader = userService.getUser();

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MILLISECOND, QueueConfig.QUEUE_EXPIRATION);
        Date overdue = calendar.getTime();

        UserBook userBook = new UserBook(now, overdue, UserBookUtil.RESERVATION, book, reader);

        userBookDAO.save(userBook);

        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(now, reader.getId(), isbn, UserBookUtil.RESERVATION);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);

        int id = userBook.getId();
        rabbitTemplate.convertAndSend(QueueConfig.DELAY_QUEUE_PER_QUEUE_TTL_NAME, id);
    }

    public int reserveCancel(int id)
    {
        UserBook userBook = userBookDAO.findById(id).get();

        if (userBook.getStatus() != UserBookUtil.RESERVATION)
            return 0;

        userBook.getBook().addNumber(1);
        userBook.setStatus(UserBookUtil.RESERVATION_CANCEL);
        userBookDAO.save(userBook);
        return 1;
    }

    public UserBook appointment(int id)
    {
        return userBookDAO.findById(id).get();
    }

    public Page<UserFavoriteBook> queryFavoriteBooks(int start, int size)
    {
        User reader = userService.getUser();
        Pageable pageable = PageableUtil.pageable(false, "id", start, size);
        return userFavoriteBookDAO.findAllByUser(reader, pageable);
    }

    public void addFavoriteBook(String isbn)
    {
        Book book = bookDAO.findByIsbn(isbn);
        User reader = userService.getUser();
        UserFavoriteBook fb = new UserFavoriteBook(book, reader);
        userFavoriteBookDAO.save(fb);
    }

    public void deleteFavoriteBook(String isbn)
    {
        Book book = bookDAO.findByIsbn(isbn);
        User reader = userService.getUser();
        UserFavoriteBook fb = userFavoriteBookDAO.findByUserAndBook(reader, book);
        fb.setBook(null);
        fb.setUser(null);
        userFavoriteBookDAO.delete(fb);
    }

    public void writeReview(String isbn, String review, String title)
    {
        Book bk = bookDAO.findByIsbn(isbn);
        Review rv = new Review(review, title, new Date(), bk, userService.getUser());
        reviewDAO.save(rv);
    }

    public Page<Review> bookReview(int start, int size, String isbn)
    {
        Book book = bookDAO.findByIsbn(isbn);
        Pageable pageable = PageableUtil.pageable(false, "date", start, size);
        return reviewDAO.findAllByBook(book, pageable);
    }

    public void deleteReview(int rid)
    {
        Review review = reviewDAO.findById(rid);
        review.setBook(null);
        review.setUser(null);
        reviewDAO.delete(review);
    }

    public int renew(int id)
    {
        UserBkunit userBkunit = userBkunitDAO.findById(id);

        if (userBkunit.getStatus() == UserBkunitUtil.RENEW)
            return -1;

        if (userBkunit.getStatus() == UserBkunitUtil.OVERDUE)
            return -2;

        if (userBkunit.getStatus() != UserBkunitUtil.BORROWED)
            return -3;

        userBkunit.setStatus(UserBkunitUtil.RENEW);
        Date borrowDate = userBkunit.getBorrowDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowDate);
        calendar.add(Calendar.DATE, 2 * globalUtilService.getMaxBorrowDays());
        userBkunit.setReturnDate(calendar.getTime());
        userBkunitDAO.save(userBkunit);
        return 0;
    }

    public boolean isFavoriteBook(String isbn)
    {
        User user = userService.getUser();
        Book book = bookDAO.findByIsbn(isbn);
        return userFavoriteBookDAO.findByUserAndBook(user, book) != null;
    }
}
