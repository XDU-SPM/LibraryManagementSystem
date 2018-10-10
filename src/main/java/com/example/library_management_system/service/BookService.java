package com.example.library_management_system.service;

import com.example.library_management_system.bean.Bkunit;
import com.example.library_management_system.bean.Book;
import com.example.library_management_system.dao.BkunitDAO;
import com.example.library_management_system.dao.BookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService
{
    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BkunitDAO bkunitDAO;

    public Page<Book> searchBook(String string, int type, int start, int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "frequency");
        Pageable pageable = PageRequest.of(start, size, sort);
        string = "%" + string + "%";
        switch (type)
        {
            case 0:     // isbn
                return bookDAO.findByIsbnLike(string, pageable);
            case 1:     // title
                return bookDAO.findByTitleLike(string, pageable);
            case 2:     // author
                return bookDAO.findByAuthorLike(string, pageable);
        }
        return null;
    }

    public Book bookInfo(String isbn)
    {
        return bookDAO.findByIsbn(isbn);
    }

    public Bkunit searchBkunit(String id)
    {
        Optional<Bkunit> bkunitOptional = bkunitDAO.findById(id);
        if (bkunitOptional.isPresent())
            return bkunitDAO.findById(id).get();
        return null;
    }
}
