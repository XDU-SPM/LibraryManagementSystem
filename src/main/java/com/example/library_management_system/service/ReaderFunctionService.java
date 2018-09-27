package com.example.library_management_system.service;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.bean.UserFavoriteBook;
import com.example.library_management_system.dao.BookDAO;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.dao.UserFavoriteBookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @ author Captain
 * @ date 2018/9/26
 * @ description as bellow.
 */


@Service
public class ReaderFunctionService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private UserFavoriteBookDAO userFavoriteBookDAO;

    @Autowired
    private BookDAO bookDAO;

    public Page<UserBkunit> queryborrowedBooks(int start, int size) {
        User reader = userService.getUser();
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<UserBkunit> page = userBkunitDAO.findAllByUser(reader, pageable);
        return page;
    }

    public Page<UserFavoriteBook> queryFavoriteBooks(int start, int size) {
        User reader = userService.getUser();
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<UserFavoriteBook> page = userFavoriteBookDAO.findAllByUser(reader, pageable);
        return page;
    }


    public boolean addFavoriteBook(Book book) {
        try {
            User reader = userService.getUser();
            UserFavoriteBook fb = userFavoriteBookDAO.findByUserandAndBook(reader,book);
            if (fb == null) userFavoriteBookDAO.save(fb);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deleteFavoriteBook(Book book) {
        try {
            User reader = userService.getUser();
            UserFavoriteBook fb = userFavoriteBookDAO.findByUserandAndBook(reader,book);
            if (fb != null) userFavoriteBookDAO.delete(fb);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
