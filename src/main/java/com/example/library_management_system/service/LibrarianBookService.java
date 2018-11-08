package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.controller.LocaleMessageSourceService;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
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
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private PunishmentDAO punishmentDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ReturnHistoryDAO returnHistoryDAO;

    @Autowired
    private LocationDAO locationDAO;

    @Resource
    private LocaleMessageSourceService localeMessageSourceService;

    public Set<String> addBkunit(Book book)
    {
        // TODO: 2018/11/8 if no isbn
        if (book.getIsbn().equals("") || book.getIsbn() == null)
            book.setIsbn(String.valueOf(System.currentTimeMillis()));
        else
            book.setIsbn(book.getIsbn().trim());
        Book book1 = bookDAO.findByIsbn(book.getIsbn());
        if (book1 == null)
        {
            book1 = book;
            bookDAO.save(book1);
        }
        else
            book1.addNumber(book.getNumber());

        Set<String> ids = new HashSet<>();
        int number = book.getNumber();
        Location location = locationDAO.findByName(book.getPosition());
        for (int i = 0; i < number; i++)
        {
            String id = BkunitUtil.generateBarCode();
            ids.add(id);
            Bkunit bkunit = new Bkunit(id, book1, location);
            bkunitDAO.save(bkunit);
        }
        return ids;
    }

    public void deleteBkunit(String id)
    {
        Bkunit bkunit = bkunitDAO.findById(id).get();
        int status = bkunit.getStatus();
        bkunit.setStatus(BkunitUtil.LOST);

        Book book = bkunit.getBook();
        if (status == BkunitUtil.NORMAL)
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
        Set<Bkunit> set = bkunitDAO.findAllByStatusNot(BkunitUtil.LOST);
        for (Bkunit bkunit : set)
            setBkunitStatus(bkunit);
        return set;
    }

    private void setBkunitStatus(Bkunit bkunit)
    {
        switch (bkunit.getStatus())
        {
            case BkunitUtil.BORROWED:
                bkunit.setStatus1(localeMessageSourceService.getMessage("borrowedStatus"));
                break;
            case BkunitUtil.NORMAL:
                bkunit.setStatus1(localeMessageSourceService.getMessage("normalStatus"));
                break;
            case BkunitUtil.RESERVED:
                bkunit.setStatus1(localeMessageSourceService.getMessage("reservedStatus"));
                break;
        }
    }

    public void saveBook(Book tmp, String id)
    {
        Book book = bookDAO.findByIsbn(tmp.getIsbn());

        book.setTitle(tmp.getTitle());
        book.setAuthor(tmp.getAuthor());
//        book.setPosition(tmp.getPosition());
        book.setPublisher(tmp.getPublisher());
        book.setPublishDate(tmp.getPublishDate());
        book.setBrief(tmp.getBrief());
        book.setPrice(tmp.getPrice());
        book.setCategory(tmp.getCategory());

        Bkunit bkunit = bkunitDAO.findById(id).get();
        if (bkunit.getLocation() == null || !bkunit.getLocation().getName().equals(tmp.getPosition()))
        {
            Location location = locationDAO.findByName(tmp.getPosition());
            bkunit.setLocation(location);
            bkunitDAO.save(bkunit);
        }

        bookDAO.save(book);
    }

    /**
     * 不存在已经被借走的情况
     * reader 是拿着书过来的，因此不存在书不存在的情况
     * 系统一定会为 reader 把书留下，因此不会存在这本书被预约的情况
     *
     * @param id
     * @param number
     * @return
     */
    public int lend(String id, String number)
    {
        User reader = userDAO.findByNumber(number);
        if (reader == null)
            return -1;

        if (reader.getBorrowNumber() >= globalUtilService.getMaxBorrowNum())
            return -2;

        Optional<Bkunit> optional = bkunitDAO.findById(id);
        if (!optional.isPresent())
            return -3;
        Bkunit bkunit = optional.get();

        if (bkunit.getStatus() == BkunitUtil.BORROWED)
            return -4;

        if (bkunit.getStatus() == BkunitUtil.LOST)
            return -5;

        UserBkunit userBkunit = userBkunitDAO.findByUserAndBkunitAndStatus(reader, bkunit, BkunitUtil.RESERVED);
        // TODO: 2018/11/8 add this case
        if (bkunit.getStatus() == BkunitUtil.RESERVED && userBkunit != null)
            return -6;

        Book book = bkunit.getBook();
        book.addFrequency();

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, globalUtilDAO.findById(1).get().getMAX_BORROW_DAYS());
        Date overdue = calendar.getTime();

        if (userBkunit == null)
        {
            userBkunit = new UserBkunit(now, overdue, UserBkunitUtil.BORROWED, bkunit, reader);
            book.addNumber(-1);
        }
        else
        {
            userBkunit.setBorrowDate(now);
            userBkunit.setReturnDate(overdue);
            userBkunit.setStatus(UserBkunitUtil.BORROWED);
        }

        bookDAO.save(book);

        bkunit.setStatus(BkunitUtil.BORROWED);
        reader.addBookNumber(1);
        userBkunitDAO.save(userBkunit);

        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(new Date(), reader.getId(), bkunit.getId(), UserBkunitUtil.BORROWED);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);

        return 0;
    }

    public ReturnMessage getReturnMessage(String id, int damage)
    {
        return new ReturnMessage(getBkunitOverdueMoney(id), getDamagePercentage(damage), getBkunitPrice(id));
    }

    private double getBkunitOverdueMoney(String id)
    {
        id = id.trim();
        Optional<Bkunit> optional = bkunitDAO.findById(id);
        if (!optional.isPresent())
            return 0;

        Bkunit bkunit = optional.get();
        // TODO: 2018/11/8 fine
        if (bkunit.getStatus() != BkunitUtil.BORROWED)
            return 0;

        UserBkunit userBkunit = userBkunitDAO.findByBkunitAndStatusBetween(bkunit, UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW);
        User reader = userBkunit.getUser();

        Date now = new Date();
        Set<Account> accounts = accountDAO.findAllByUidAndBkidAndTypeAndDateBetween(reader.getId(), bkunit.getId(), AccountUtil.OVERDUE, userBkunit.getBorrowDate(), now);
        double overdueMoney = 0;
        for (Account account : accounts)
            overdueMoney += account.getMoney();
        return overdueMoney;
    }

    private double getDamagePercentage(int damage)
    {
        return punishmentDAO.findById(damage2status(damage)).get().getRate();
    }

    private double getBkunitPrice(String id)
    {
        id = id.trim();
        Optional<Bkunit> optional = bkunitDAO.findById(id);
        if (!optional.isPresent())
            return 0;

        Bkunit bkunit = optional.get();
        return bkunit.getBook().getPrice();
    }

    private int damage2status(int damage)
    {
        switch (damage)
        {
            case 0:
                return BkunitUtil.NO_DAMAGE;
            case 1:
                return BkunitUtil.MILD_DAMAGE;
            case 2:
                return BkunitUtil.MODERATE_DAMAGE;
            case 3:
                return BkunitUtil.MAJOR_DAMAGE;
            case 4:
                return BkunitUtil.LOST;
        }
        return 0;
    }

    public int returnBook(String id, int damage, double money)
    {
        id = id.trim();
        Optional<Bkunit> optional = bkunitDAO.findById(id);
        if (!optional.isPresent())
            return -1;

        Bkunit bkunit = optional.get();

        if (bkunit.getStatus() == BkunitUtil.LOST)
        {
            UserBkunit userBkunit = userBkunitDAO.findByBkunitAndStatusBetween(bkunit, UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW);

            if (userBkunit == null)
                return -2;

            User reader = userBkunit.getUser();
            reader.addBookNumber(-1);

            Date now = new Date();
            userBkunit.setReturnDate(now);
            userBkunit.setStatus(UserBkunitUtil.RETURNED);

            BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(now, reader.getId(), bkunit.getId(), UserBkunitUtil.RETURNED);
            bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);

            // TODO: 2018/11/8 fine
            Set<Account> accounts = accountDAO.findAllByUidAndBkidAndTypeAndDateBetween(reader.getId(), bkunit.getId(), AccountUtil.OVERDUE, userBkunit.getBorrowDate(), now);
            double overdueMoney = 0;
            for (Account account : accounts)
                overdueMoney += account.getMoney();

            reader.addMoney(overdueMoney);
            userBkunitDAO.save(userBkunit);

            ReturnHistory returnHistory = new ReturnHistory(reader.getId(), userBkunit, 0);
            returnHistoryDAO.save(returnHistory);

            return -3;
        }

        if (bkunit.getStatus() != BkunitUtil.BORROWED)
            return -2;

        Book book = bkunit.getBook();
        book.addNumber(1);

        UserBkunit userBkunit = userBkunitDAO.findByBkunitAndStatusBetween(bkunit, UserBkunitUtil.BORROWED, UserBkunitUtil.RENEW);
        User reader = userBkunit.getUser();
        bkunit.setStatus(BkunitUtil.NORMAL);
        reader.addBookNumber(-1);

        int damageStatus = damage2status(damage);
        if (damageStatus == BkunitUtil.LOST)
        {
            bkunit.setStatus(BkunitUtil.LOST);
            book.addNumber(-1);
        }
        bookDAO.save(book);

        double damageMoney = bkunit.getBook().getPrice() * (punishmentDAO.findById(damageStatus).get().getRate() - punishmentDAO.findById(bkunit.getDamageStatus()).get().getRate());

        // TODO: 2018/11/8 notify
        if (damageMoney < 0)
            return -4;

        Date now = new Date();

        if (damageMoney != 0)
        {
            Account account = new Account(AccountUtil.DAMAGE, reader.getId(), damageMoney, id, now);
            accountDAO.save(account);
        }
        bkunit.setDamageStatus(damageStatus);
        userBkunit.setReturnDate(now);
        userBkunit.setStatus(UserBkunitUtil.RETURNED);

        BkunitOperatingHistory bkunitOperatingHistory = new BkunitOperatingHistory(now, reader.getId(), bkunit.getId(), UserBkunitUtil.RETURNED);
        bkunitOperatingHistoryDAO.save(bkunitOperatingHistory);

        Set<Account> accounts = accountDAO.findAllByUidAndBkidAndTypeAndDateBetween(reader.getId(), bkunit.getId(), AccountUtil.OVERDUE, userBkunit.getBorrowDate(), now);
        double overdueMoney = 0;
        for (Account account : accounts)
            overdueMoney += account.getMoney();

        reader.addMoney(overdueMoney);
        userBkunitDAO.save(userBkunit);

        if (!doubleEqual(money, damageMoney + overdueMoney))
            return -4;

        Account account = new Account(AccountUtil.FINE, reader.getId(), money, null, now);
        accountDAO.save(account);

        ReturnHistory returnHistory = new ReturnHistory(reader.getId(), userBkunit, damageMoney + overdueMoney);
        returnHistoryDAO.save(returnHistory);

        return 0;
    }

    private boolean doubleEqual(double d1, double d2)
    {
        return Math.abs(d1 - d2) < 0.01;
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

    public void removeBookCategory(String isbn, String categoryName)
    {
        Category category = categoryDAO.findByName(categoryName);
        if (category == null)
            return;

        Book book = bookDAO.findByIsbn(isbn);
        book.getCategories().remove(category);
        bookDAO.save(book);
    }

    public void addBookLocation(String isbn, String locationName)
    {
        Location location = locationDAO.findByName(locationName);
        if (location == null)
        {
            location = new Location(locationName);
            locationDAO.save(location);
        }

        Book book = bookDAO.findByIsbn(isbn);
        book.getLocations().add(location);
        bookDAO.save(book);
    }

    public void removeBookLocation(String isbn, String locationName)
    {
        Location location = locationDAO.findByName(locationName);
        if (location == null)
            return;

        Book book = bookDAO.findByIsbn(isbn);
        book.getLocations().remove(location);
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

    // TODO: 2018/11/8 need modify
    public Set<UserBkunit> getReserves()
    {
        Set<UserBkunit> set = userBkunitDAO.findAllByStatusBetween(UserBkunitUtil.RESERVED, UserBkunitUtil.RESERVATION_CANCEL);
        for (UserBkunit userBkunit : set)
            setUserBookStatus(userBkunit);
        return set;
    }

    private void setUserBookStatus(UserBkunit userBkunit)
    {
        switch (userBkunit.getStatus())
        {
            case UserBkunitUtil.RESERVED:
                userBkunit.setStatus1(localeMessageSourceService.getMessage("reservation"));
                break;
            case UserBkunitUtil.RESERVATION_FAIL:
                userBkunit.setStatus1(localeMessageSourceService.getMessage("reservation_fail"));
                break;
            case UserBkunitUtil.RESERVATION_CANCEL:
                userBkunit.setStatus1(localeMessageSourceService.getMessage("reservation_cancel"));
                break;
        }
    }

    public Set<BkunitOperatingHistory> getBkunitOperatingHistory(int status)
    {
        Set<BkunitOperatingHistory> bkunitOperatingHistories = bkunitOperatingHistoryDAO.findAllByStatus(status);
        for (BkunitOperatingHistory bkunitOperatingHistory : bkunitOperatingHistories)
            updateBkunitOperatingHistory(bkunitOperatingHistory);
        return bkunitOperatingHistories;
    }

    public Page<BkunitOperatingHistory> getBkunitOperatingHistory(int start, int size, int status)
    {
        Pageable pageable = PageableUtil.pageable(false, "date", start, size);
        Page<BkunitOperatingHistory> bkunitOperatingHistories = bkunitOperatingHistoryDAO.findAllByStatus(status, pageable);
        for (BkunitOperatingHistory bkunitOperatingHistory : bkunitOperatingHistories)
            updateBkunitOperatingHistory(bkunitOperatingHistory);
        return bkunitOperatingHistories;
    }

    private void updateBkunitOperatingHistory(BkunitOperatingHistory bkunitOperatingHistory)
    {
        String username;
        User user;
        if ((user = userDAO.findById(bkunitOperatingHistory.getUid())) == null)
            username = localeMessageSourceService.getMessage("bedeleted");
        else
            username = user.getUsername();
        bkunitOperatingHistory.setUsername(username);

        Optional<Bkunit> optional = bkunitDAO.findById(bkunitOperatingHistory.getBuid());
        String bookName;
        if (!optional.isPresent())
            bookName = localeMessageSourceService.getMessage("bedeleted");
        else
            bookName = optional.get().getBook().getTitle();
        bkunitOperatingHistory.setBookName(bookName);
    }
}
