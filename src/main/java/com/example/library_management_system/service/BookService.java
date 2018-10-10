package com.example.library_management_system.service;

import com.example.library_management_system.bean.Bkunit;
import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.Category;
import com.example.library_management_system.dao.BkunitDAO;
import com.example.library_management_system.dao.BookDAO;
import com.example.library_management_system.dao.CategoryDAO;
import com.example.library_management_system.utils.PageableUtil;
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

    @Autowired
    private CategoryDAO categoryDAO;

    public Page<Book> searchBook(String string, int type, int start, int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "frequency");
        Pageable pageable = PageRequest.of(start, size, sort);
        string = "%" + string + "%";
        switch (type)
        {
            case 1:     // isbn
                return bookDAO.findByIsbnLike(string, pageable);
            case 2:     // title
                return bookDAO.findByTitleLike(string, pageable);
            case 3:     // author
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

    public Page<Book> showBook(int start, int size, String category)
    {
        Category c = categoryDAO.findByName(category);
        Pageable pageable = PageableUtil.pageable(false, "frequency", start, size);
        if (category != null)
            return bookDAO.findByCategoriesContaining(c, pageable);
        return bookDAO.findAll(pageable);
    }
}
