package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserService
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private GlobalUtilService globalUtilService;

    @Autowired
    private UserFavoriteBookDAO userFavoriteBookDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserBookDAO userBookDAO;

    @Autowired
    private GithubAvatarGenerator githubAvatarGenerator;

    @Value("${root.path}")
    private String rootPath;

    public User getUser()
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.findByUsername(userDetails.getUsername());
    }

    public void registerService(User user, String roleName)
    {
//        user.setPassword(MD5Util.encode(user.getPassword()));
        if (RoleUtil.ROLE_READER_CHECK.equals(roleName))
            user.setPassword(UserUtil.READER_PASSWORD_DEFAULT);
        else if (RoleUtil.ROLE_LIBRARIAN_CHECK.equals(roleName))
            user.setPassword(UserUtil.LIBRARIAN_PASSWORD_DEFAULT);
        Role role = roleDAO.findByName(roleName);
        user.getRoles().add(role);
        userDAO.save(user);

        user.setAvatarPath(githubAvatarGenerator.generateAvatar(user.getId()));
        userDAO.save(user);

        if (RoleUtil.ROLE_READER_CHECK.equals(roleName))
        {
            Account account = new Account(AccountUtil.DEPOSIT, user.getId(), globalUtilService.getRegisterMoney(), null, new Date());
            accountDAO.save(account);
        }
    }

    /*public String accept(int id)
    {
        User user = userDAO.getOne(id);
        if (user.getRoles().contains(roleDAO.findByName(RoleUtil.ROLE_READER_CHECK)))
        {
            user.getRoles().add(roleDAO.findByName(RoleUtil.ROLE_READER));
            userDAO.save(user);
            return RoleUtil.ROLE_READER_CHECK;
        }
        else if (user.getRoles().contains(roleDAO.findByName(RoleUtil.ROLE_LIBRARIAN_CHECK)))
        {
            user.getRoles().add(roleDAO.findByName(RoleUtil.ROLE_LIBRARIAN));
            userDAO.save(user);
            return RoleUtil.ROLE_LIBRARIAN_CHECK;
        }
        return null;
    }*/

    public boolean renew(int id)
    {
        UserBkunit userBkunit = userBkunitDAO.findById(id);
        if (userBkunit.getStatus() == UserBkunitUtil.BORROWED)
        {
            //更新续借图书的状态和天数
            userBkunit.setStatus(UserBkunitUtil.RENEW);
            Date borrowDate = userBkunit.getBorrowDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrowDate);
            calendar.add(Calendar.DATE, 2 * globalUtilService.getMaxBorrowDays());
            userBkunit.setReturnDate(calendar.getTime());
            userBkunitDAO.save(userBkunit);
            return true;
        }
        return false;
    }

    //还书
    /*public boolean returnbook(int uid, int buid)
    {
        User user = userDAO.findById(uid);
        UserBkunit userBkunit = userBkunitDAO.findByUserAndBkunit(null, null);
        //只有在借书、续借、超期状态下才可以还书
        if (userBkunit.getStatus() == UserBkunitUtil.BORROWED || userBkunit.getStatus() == UserBkunitUtil.OVERDUE ||
                userBkunit.getStatus() == UserBkunitUtil.RENEW)
        {
            //判断是否超期
            if (userBkunit.getStatus() == UserBkunitUtil.OVERDUE)
            {
                //需要交的钱
                double money = OverduetimeUtil.getoverduetime(userBkunit.getBorrowDate(), userBkunit.getReturnDate()) * 1;
                user.setMoney(user.getMoney() - money);
                Account account = new Account();
                account.setDate(new Date(System.currentTimeMillis()));
                account.setMoney(money);
                account.setType(AccountUtil.OVERDUE);
//                account.setUser(user);
                accountDAO.save(account);
            }
            //可借图书天数加1
            user.setBUL(user.getBUL() + 1);
            userDAO.save(user);
            //修改借书状态
            userBkunit.setStatus(UserBkunitUtil.RETURNED);
            userBkunitDAO.save(userBkunit);
            return true;
        }
        return false;
    }*/

    public void saveUser(User tmp)
    {
        User user = getUser();

        user.setUsername(tmp.getUsername());
        user.setName(tmp.getName());
        user.setEmail(tmp.getEmail());

        userDAO.save(user);
    }

    public int forgetPassword(String username)
    {
        User user = userDAO.findByUsername(username);

        if (user == null)
            return -1;

        try
        {
            MailUtil.sendmail(user.getEmail(), user.getPassword(), "password");
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            return -2;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return -2;
        }
        return 0;
    }

    public void modifyPassword(String password)
    {
        User user = getUser();
        user.setPassword(password);
        userDAO.save(user);
    }

    public User searchUser(String username)
    {
        return userDAO.findByUsername(username);
    }

    public int deleteUser(int id)
    {
        User user = userDAO.findById(id);

        if (user.getMoney() < 0)
            return -1;


        Set<UserBkunit> userBkunits = user.getUserBkunits();

        for (UserBkunit userBkunit : userBkunits)
        {
            if (userBkunit.getStatus() == UserBkunitUtil.BORROWED)
                return -2;
        }

        for (UserBkunit userBkunit : userBkunits)
        {
            userBkunit.setUser(null);
            userBkunit.setBkunit(null);
        }
        userBkunitDAO.saveAll(userBkunits);

        Set<Review> reviews = user.getReviews();
        for (Review review : reviews)
        {
            review.setUser(null);
            review.setBook(null);
        }
        reviewDAO.saveAll(reviews);

        Set<UserFavoriteBook> userFavoriteBooks = user.getUserFavoriteBooks();
        for (UserFavoriteBook userFavoriteBook : userFavoriteBooks)
        {
            userFavoriteBook.setUser(null);
            userFavoriteBook.setBook(null);
        }
        userFavoriteBookDAO.saveAll(userFavoriteBooks);

        Set<UserBook> userBooks = user.getUserBooks();
        for (UserBook userBook : userBooks)
        {
            userBook.setUser(null);
            userBook.setBook(null);
        }
        userBookDAO.saveAll(userBooks);

        userDAO.deleteById(id);

        userBkunitDAO.deleteAll(userBkunits);
        reviewDAO.deleteAll(reviews);
        userFavoriteBookDAO.deleteAll(userFavoriteBooks);
        userBookDAO.deleteAll(userBooks);
        return 0;
    }

    public Page<User> showAllUser(int start, int size, String role)
    {
        Pageable pageable = PageableUtil.pageable(true, "id", start, size);
        Page<User> users;
        if (role != null)
        {
            Role r = roleDAO.findByName(role);
            users = userDAO.findByRolesContaining(r, pageable);
        }
        else
            users = userDAO.findAll(pageable);
        for (User user : users)
            addRole(user);
        return users;
    }

    public List<User> showAllUser(String role)
    {
        List<User> users;
        if (role != null)
        {
            Role r = roleDAO.findByName(role);
            users = userDAO.findByRolesContaining(r);
        }
        else
            users = userDAO.findAll();
        for (User user : users)
            addRole(user);
        return users;
    }

    private void addRole(User user)
    {
        Iterator<Role> iterator = user.getRoles().iterator();
        switch (iterator.next().getId())
        {
            case 2:
                user.setRole("admin");
                break;
            case 4:
                user.setRole("reader");
                break;
            case 5:
                user.setRole("librarian");
                break;
        }
    }

    public User showUser(int id)
    {
        return userDAO.findById(id);
    }

    public boolean userExist(String username)
    {
        return userDAO.findByUsername(username) != null;
    }

    public void modifyAvatarPath(MultipartFile file)
    {
        User user = getUser();
        String fileName = file.getOriginalFilename();
        String type = fileName.substring(fileName.lastIndexOf('.'));
        String filePath = "" + MD5Util.encode("" + System.currentTimeMillis() + user.getId()) + type;
        FileUtil.saveFile(file, new File(rootPath + filePath));
        user.setAvatarPath("../images/" + filePath);
        userDAO.save(user);
    }
}
