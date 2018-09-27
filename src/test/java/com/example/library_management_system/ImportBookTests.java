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

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportBookTests
{
    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Before
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void before()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/douban_db?characterEncoding=UTF-8",
                "root", "admin");
             Statement statement = connection.createStatement())
        {
            int state1, state2;
            String sql = "select * from allbooks";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next())
            {
                String title = null;
                try
                {
                    state1 = state2 = 1;
                    title = resultSet.getString("title");
                    double score = Double.parseDouble(resultSet.getString("scor"));
                    String author = resultSet.getString("author");
                    String time = resultSet.getString("time");
                    String publisher = resultSet.getString("publish");
                    String categoryName = resultSet.getString("tag");
                    String brief = resultSet.getString("brief");
                    String ISBN = resultSet.getString("ISBN");
//                    Optional<Book> bookOptional = bookDAO.findById(ISBN);
                    Book book = bookDAO.findByIsbn(ISBN);
//                    if (bookOptional.isPresent())
//                        book = bookOptional.get();
                    if (book == null)
                    {
                        book = new Book(ISBN, title, score, time, publisher, author);
                        state1 = 0;
                    }
                    Category category = categoryDAO.findByName(categoryName);
                    if (category == null)
                    {
                        category = new Category(categoryName);
                        state2 = 0;
                    }
                    if (state1 == 1 && state2 == 1)
                        continue;
                    else if (state1 == 1)
                    {
                        book.getCategories().add(category);
                        bookDAO.save(book);
                    }
                    else
                    {
                        category.getBooks().add(book);
                        categoryDAO.save(category);
                    }
                }
                catch (Exception e)
                {
                    System.out.println(title);
                    e.printStackTrace();
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void contextLoads()
    {
        Category category = categoryDAO.findById(1).get();
        System.out.println(category.getBooks().size());
    }
}
