package com.example.library_management_system.service;

import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.BkunitDAO;
import com.example.library_management_system.dao.BookDAO;
import com.example.library_management_system.dao.CategoryDAO;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.utils.BkunitUtil;
import com.example.library_management_system.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    public void addBkunit(Book book, int number, String categoryName, MultipartFile file)
    {
        Book book1 = bookdao.findByIsbn(book.getIsbn());
        if (book1 == null)
        {
            book1 = book;
            String fileName = file.getOriginalFilename();
            String[] tmps = fileName.split("\\.");
            String type = tmps[tmps.length - 1];
            String coverPath = "src/main/resources/static/upload/" + book.getIsbn() + "." + type;
            FileUtil.saveFile(file, new File(coverPath));

            Category category = categoryDAO.findByName(categoryName);
            if (category == null)
            {
                category = new Category(categoryName);
                categoryDAO.save(category);
            }

            book1.setCoverPath("../upload/" + book.getIsbn() + "." + type);
            book1.getCategories().add(category);
            bookdao.save(book1);
        }
        for (int i = 0; i < number; i++)
        {
            String id = String.valueOf(System.currentTimeMillis());
            Bkunit bkunit = new Bkunit(id, book1);
            bkunitdao.save(bkunit);
        }
    }

    public void deleteBkunit(String id)
    {
        Bkunit bkunit = bkunitdao.findById(id).get();
        bkunit.setStatus(BkunitUtil.LOST);
        bkunitdao.save(bkunit);
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
        Sort sort = new Sort(Sort.Direction.ASC, "isbn");
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

    public Book bookInfo(String isbn)
    {
        return bookdao.findByIsbn(isbn);
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
