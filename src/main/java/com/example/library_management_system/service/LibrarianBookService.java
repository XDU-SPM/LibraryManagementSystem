package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.BkunitDAO;
import com.example.library_management_system.dao.BookDAO;
import com.example.library_management_system.dao.CategoryDAO;
import com.example.library_management_system.dao.UserBkunitDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;

@Service
public class LibrarianBookService
{

    @Autowired
    private BkunitDAO bkunitdao;
    @Autowired
    private BookDAO bookdao;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private UserBkunitDAO userBkunitDAO;

    public void addBkunit(Bkunit bkunit)
    {
        bkunitdao.save(bkunit);
    }

    public void deleteBkunit(String id)
    {
        Bkunit bkunit = bkunitdao.findById(id).get();
        Set<UserBkunit> userBkunits = bkunit.getUserBkunits();
        for (UserBkunit userBkunit : userBkunits)
        {
            userBkunit.setBkunit(null);
            userBkunit.setUser(null);
        }
        userBkunitDAO.saveAll(userBkunits);
        bkunitdao.deleteById(id);
    }

    public Page<Bkunit> showbkunit(int start, int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        return bkunitdao.findAll(pageable);
    }

    public Page<Book> showbook(int start, int size, String category)
    {
        Category c = categoryDAO.findByName(category);
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        return bookdao.findByCategoriesContaining(c, pageable);
    }

    public Bkunit searchbyid(String id)
    {
        Optional<Bkunit> bkunitOptional = bkunitdao.findById(id);
        if (bkunitOptional.isPresent())
            return bkunitdao.findById(id).get();
        return null;
    }

    public void changeinfo(Bkunit bkunit)
    {
        bkunitdao.save(bkunit);
    }

    public void addBook(Book book)
    {
        bookdao.save(book);
    }

    public boolean isexist(Book book)
    {
        return bookdao.existsById(book.getIsbn());
    }
}
