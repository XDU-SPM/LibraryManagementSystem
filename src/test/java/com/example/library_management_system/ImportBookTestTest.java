package com.example.library_management_system;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.Category;
import com.example.library_management_system.dao.BookDAO;
import com.example.library_management_system.dao.CategoryDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportBookTestTest
{
    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Before
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void before()
    {
        Book book = new Book("444");
        Category category = new Category("222");
        category.getBooks().add(book);
        categoryDAO.save(category);

        Book book1 = new Book("555");
        Category category1 = categoryDAO.findByName("222");
        if (category1 == null)
            category1 = new Category("222");
        category1.getBooks().add(book1);
        categoryDAO.save(category1);

        Book book2 = bookDAO.findByIsbn("444");
        if (book2 == null)
        {
            System.out.println("233");
            book2 = new Book("444");
        }
        Category category2 = new Category("333");
        book2.getCategories().add(category2);
        bookDAO.save(book2);
    }

    @Test
    public void contextLoads()
    {
        Book book = bookDAO.findById("444").get();
        System.out.println(book.getCategories().size());
    }
}
