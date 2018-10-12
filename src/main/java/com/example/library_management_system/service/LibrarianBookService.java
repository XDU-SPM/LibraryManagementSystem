package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
public class LibrarianBookService
{
    @Autowired
    private BkunitDAO bkunitDAO;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private BkunitOperatingHistoryDAO bkunitOperatingHistoryDAO;

    @Autowired
    private GlobalUtilDAO globalUtilDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GlobalUtilService globalUtilService;

    @Autowired
    private UserBookDAO userBookDAO;

    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private PunishmentDAO punishmentDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ReturnHistoryDAO returnHistoryDAO;

    public Set<String> addBkunit(Book book, String categoryName)
    {
        Book book1 = bookDAO.findByIsbn(book.getIsbn());
        if (book1 == null)
        {
            book1 = book;

            Category category = categoryDAO.findByName(categoryName);
            if (category == null)
            {
                category = new Category(categoryName);
                categoryDAO.save(category);
            }

            book1.getCategories().add(category);
            bookDAO.save(book1);
        }
        else
            book1.addNumber(book.getNumber());

        Set<String> ids = new HashSet<>();
        int number = book.getNumber();
        for (int i = 0; i < number; i++)
        {
            String id = BkunitUtil.generateBarCode();
            ids.add(id);
            Bkunit bkunit = new Bkunit(id, book1);
            bkunitDAO.save(bkunit);
        }
        return ids;
    }

    public void deleteBkunit(String id)
    {
        Bkunit bkunit = bkunitDAO.findById(id).get();
        bkunit.setStatus(BkunitUtil.LOST);

        Book book = bkunit.getBook();
        book.addNumber(-1);
        bookDAO.save(book);

        bkunitDAO.save(bkunit);

        User librarian = userService.getUser();
        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), librarian.getId(), id, UserBkunitUtil.DELETE);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);
    }

    public Page<Bkunit> showBkunit(int start, int size)
    {
        Pageable pageable = PageableUtil.pageable(false, "id", start, size);
        return bkunitDAO.findAllByStatusNot(BkunitUtil.LOST, pageable);
    }

    public Set<Bkunit> showBkunit()
    {
        return bkunitDAO.findAllByStatusNot(BkunitUtil.LOST);
    }

    public void saveBook(Book tmp)
    {
        Book book = bookDAO.findByIsbn(tmp.getIsbn());

        book.setTitle(tmp.getTitle());
        book.setAuthor(tmp.getAuthor());
        book.setPosition(tmp.getPosition());
        book.setPublisher(tmp.getPublisher());
        book.setPublishDate(tmp.getPublishDate());
        book.setBrief(tmp.getBrief());
        book.setPrice(tmp.getPrice());

        bookDAO.save(book);
    }

    /**
     * 不存在已经被借走的情况
     * reader 是拿着书过来的，因此不存在书不存在的情况
     * 系统一定会为 reader 把书留下，因此不会存在这本书被预约的情况
     *
     * @param id
     * @param username
     * @return
     */
    public int lend(String id, String username)
    {
        User reader = userDAO.findByUsername(username);
        if (reader == null)
            return -1;

        if (reader.getBorrowNumber() >= globalUtilService.getMaxBorrowNum())
            return -2;

        Optional<Bkunit> optional = bkunitDAO.findById(id);
        if (!optional.isPresent())
            return -3;
        Bkunit bkunit = optional.get();

        if (bkunit.getStatus() != BkunitUtil.NORMAL)
            return -5;

        Book book = bkunit.getBook();
        UserBook userBook;

        if ((userBook = userBookDAO.findByUserAndBookAndStatus(reader, book, UserBookUtil.RESERVATION)) != null)
        {
            book.addNumber(1);
            userBook.setStatus(UserBkunitUtil.BORROWED);
            userBookDAO.save(userBook);
        }

        book.addNumber(-1);
        book.addFrequency();
//        bookDAO.save(book);

        UserBkunit userBkunit = userBkunitDAO.findByUserAndBkunit(reader, bkunit);

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, globalUtilDAO.findById(1).get().getMAX_BORROW_DAYS());
        Date overdue = calendar.getTime();

        if (userBkunit == null)
            userBkunit = new UserBkunit(now, overdue, UserBkunitUtil.BORROWED, bkunit, reader);
        else
        {
            userBkunit.setBorrowDate(now);
            userBkunit.setReturnDate(overdue);
            userBkunit.setStatus(UserBkunitUtil.BORROWED);
        }

        bkunit.setStatus(BkunitUtil.BORROWED);
        reader.addBookNumber(1);
        userBkunitDAO.save(userBkunit);

        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), reader.getId(), bkunit.getId(), UserBkunitUtil.BORROWED);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);

        return 0;
    }

    /**
     * reader 是拿着书过来的，因此不存在书不存在的情况
     *
     * @param id
     * @param damage
     * @return
     */
    public int returnBook(String id, int damage)
    {
        id = id.trim();
        Bkunit bkunit = bkunitDAO.findById(id).get();
        if (bkunit.getStatus() != BkunitUtil.BORROWED)
            return -2;

        Book book = bkunit.getBook();
        book.addNumber(1);

        UserBkunit userBkunit = userBkunitDAO.findByBkunitAndStatusBetween(bkunit, UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW);
        User reader = userBkunit.getUser();
        bkunit.setStatus(BkunitUtil.NORMAL);
        reader.addBookNumber(-1);
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
                book.addNumber(-1);
                break;
        }

        bookDAO.save(book);

        double damageMoney = bkunit.getBook().getPrice() * (punishmentDAO.findById(damageStatus).get().getRate() - punishmentDAO.findById(bkunit.getDamageStatus()).get().getRate());
        if (damageMoney != 0)
        {
            Account account = new Account(AccountUtil.DAMAGE, reader.getId(), damageMoney, id, new Date());
            accountDAO.save(account);
        }
        reader.addMoney(-damageMoney);
        bkunit.setDamageStatus(damageStatus);
        userBkunit.setReturnDate(new Date());
        userBkunit.setStatus(UserBkunitUtil.RETURNED);
        userBkunitDAO.save(userBkunit);

        Date now = new Date();

        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(now, reader.getId(), bkunit.getId(), UserBkunitUtil.RETURNED);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);

        Set<Account> accounts = accountDAO.findAllByUidAndBkidAndTypeAndDateBetween(reader.getId(), bkunit.getId(), AccountUtil.OVERDUE, userBkunit.getBorrowDate(), now);
        double overdueMoney = 0;
        for (Account account : accounts)
            overdueMoney += account.getMoney();

        ReturnHistory returnHistory = new ReturnHistory(reader.getId(), userBkunit, damageMoney + overdueMoney);
        returnHistoryDAO.save(returnHistory);

        return 0;
    }

    public void addBookCategory(String isbn, String categoryName)
    {
        Category category = categoryDAO.findByName(categoryName);
        if (category == null)
        {
            category = new Category(categoryName);
            categoryDAO.save(category);
        }

        Book book = bookDAO.findByIsbn(isbn);
        book.getCategories().add(category);
        bookDAO.save(book);
    }

    public long todayBorrowNumber()
    {
        OneDayApart oneDayApart = new OneDayApart();
        return bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(oneDayApart.getBefore(), oneDayApart.getAfter(), UserBkunitUtil.BORROWED);
    }

    public long todayReturnNumber()
    {
        OneDayApart oneDayApart = new OneDayApart();
        return bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(oneDayApart.getBefore(), oneDayApart.getAfter(), UserBkunitUtil.RETURNED);
    }

    public long todayOverdueNumber()
    {
        OneDayApart oneDayApart = new OneDayApart();
        return bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(oneDayApart.getBefore(), oneDayApart.getAfter(), UserBkunitUtil.OVERDUE);
    }

    public long[] statusNum()
    {
        long[] statusNum = new long[3];
        statusNum[0] = bkunitDAO.countByStatus(BkunitUtil.BORROWED);

        long normalNum = bkunitDAO.countByStatus(BkunitUtil.NORMAL);

        List<Book> books = bookDAO.findAll();
        long availableNum = 0;
        for (Book book : books)
            availableNum += book.getNumber();
        statusNum[1] = availableNum;

        statusNum[2] = normalNum - availableNum;
        return statusNum;
    }

    public List<DayBorrowReturn> dayBorrowReturns()
    {
        List<DayBorrowReturn> dayBorrowReturns = new ArrayList<>();
        OneDayApart oneDayApart = new OneDayApart();
        Stack<DayBorrowReturn> stack = new Stack<>();
        for (int i = 0; i < 7; i++)
        {
            Date before = oneDayApart.getBefore();
            Date after = oneDayApart.getAfter();
            stack.push(new DayBorrowReturn(before,
                    bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(before, after, UserBkunitUtil.BORROWED),
                    bkunitOperatingHistoryDAO.countByDateBetweenAndStatus(before, after, UserBkunitUtil.RETURNED)));
            oneDayApart.setLastDay();
        }
        while (!stack.isEmpty())
            dayBorrowReturns.add(stack.pop());
        return dayBorrowReturns;
    }

    public Page<UserBook> getReserves(int start, int size)
    {
        Pageable pageable = PageableUtil.pageable(false, "borrowDate", start, size);
        return userBookDAO.findAllByStatusBetween(UserBookUtil.RESERVATION, UserBookUtil.RESERVATION_CANCEL, pageable);
    }

    public Set<UserBook> getReserves()
    {
        return userBookDAO.findAllByStatusOrStatusOrStatusOrStatus(UserBkunitUtil.BORROWED, UserBookUtil.RESERVATION, UserBookUtil.RESERVATION_FAIL, UserBookUtil.RESERVATION_CANCEL);
    }

    public Set<BkunitOperatingHistory> getBkunitOperatingHistory(int status, boolean bkunit)
    {
        Set<BkunitOperatingHistory> bkunitOperatingHistories = bkunitOperatingHistoryDAO.findAllByStatus(status);
        for (BkunitOperatingHistory bkunitOperatingHistory : bkunitOperatingHistories)
            updateBkunitOperatingHistory(bkunitOperatingHistory, bkunit);
        return bkunitOperatingHistories;
    }

    public Page<BkunitOperatingHistory> getBkunitOperatingHistory(int start, int size, int status, boolean bkunit)
    {
        Pageable pageable = PageableUtil.pageable(false, "date", start, size);
        Page<BkunitOperatingHistory> bkunitOperatingHistories = bkunitOperatingHistoryDAO.findAllByStatus(status, pageable);
        for (BkunitOperatingHistory bkunitOperatingHistory : bkunitOperatingHistories)
            updateBkunitOperatingHistory(bkunitOperatingHistory, bkunit);
        return bkunitOperatingHistories;
    }

    private void updateBkunitOperatingHistory(BkunitOperatingHistory bkunitOperatingHistory, boolean bkunit)
    {
        bkunitOperatingHistory.setUsername(userDAO.findById(bkunitOperatingHistory.getUid()).getUsername());
        if (bkunit)
            bkunitOperatingHistory.setBookName(bkunitDAO.findById(bkunitOperatingHistory.getBuid()).get().getBook().getTitle());
        else
            bkunitOperatingHistory.setBookName(bookDAO.findByIsbn(bkunitOperatingHistory.getBuid()).getTitle());
    }

    public Set<Account> getPaidAccounts()
    {
        return null;
    }

    public Set<Account> getUnPaidAccounts()
    {
        return null;
    }
}
