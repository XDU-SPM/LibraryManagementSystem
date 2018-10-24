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

    public Page<Book> searchBook(String string, int start, int size)
    {
        Pageable pageable = PageableUtil.pageable(false, "frequency", start, size);
        string = "%" + string.trim() + "%";
        return bookDAO.findByIsbnLikeOrTitleLikeOrAuthorLike(string, string, string, pageable);
    }

    public Book bookInfo(String id)
    {
        return bkunitDAO.findById(id).get().getBook();
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
