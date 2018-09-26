package com.example.library_management_system;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.dao.BookDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportBookTests
{
    @Autowired
    private BookDAO bookDAO;

    @Test
//    @Transactional
    public void contextLoads()
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
//            int num = 0;
//            int num2 = 0;
            Set<Book> books = new HashSet<>();
            String sql = "select * from allbooks";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next())
            {
                String title = null;
                try
                {
//                    num++;
                    title = resultSet.getString("title");
                    double score = Double.parseDouble(resultSet.getString("scor"));
                    String author = resultSet.getString("author");
                    String time = resultSet.getString("time");
                    String publisher = resultSet.getString("publish");
                    String category = resultSet.getString("tag");
                    String brief = resultSet.getString("brief");
                    String ISBN = resultSet.getString("ISBN");
//                    if (bookDAO.existsById(ISBN))
//                        num2++;
                    Book book = new Book(ISBN, title, score, time, publisher, category, author);
//                    bookDAO.save(book);
                    books.add(book);
                }
                catch (Exception e)
                {
                    System.out.println(title);
                }
            }
//            System.out.println(num);
//            System.out.println(num2);
            bookDAO.saveAll(books);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
