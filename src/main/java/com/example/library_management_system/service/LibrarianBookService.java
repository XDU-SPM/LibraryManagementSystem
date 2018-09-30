package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.BkunitDAO;
import com.example.library_management_system.dao.BookDAO;
import com.example.library_management_system.dao.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
public class LibrarianBookService
{

    @Autowired
    private BkunitDAO bkunitdao;
    @Autowired
    private BookDAO bookdao;
    @Autowired
    private CategoryDAO categoryDAO;

    public void addBkunit(Bkunit bkunit)
    {
        bkunitdao.save(bkunit);

    }

    public void deleteBkunit(String id)
    {
        bkunitdao.deleteById(id);
    }

    public Page<Bkunit> showbkunit(int start, int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Bkunit> page = bkunitdao.findAll(pageable);
        return page;
    }

    public Page<Book> showbook(int start, int size, String category)
    {
        Category c = categoryDAO.findByName(category);
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Book> page = bookdao.findByCategoriesContaining(c, pageable);
        return page;
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
