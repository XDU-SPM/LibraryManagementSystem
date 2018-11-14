package com.example.library_management_system.service;

import com.example.library_management_system.bean.Book;
import com.example.library_management_system.bean.Category;
import com.example.library_management_system.dao.BookDAO;
import com.example.library_management_system.dao.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService
{
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private BookDAO bookDAO;

    public void init()
    {
        String[] categoryNames = {"Literature & Fiction", "Arts & Music", "Biographies", "Business", "Kids", "Comics",
                "Computers & Tech", "Cooking", "Hobbies & Crafts", "Edu & Reference", "Gay & Lesbian", "Health & Fitness",
                "History", "Home & Garden", "Horror", "Entertainment", "Medical", "Mysteries", "Parenting", "Social Sciences",
                "Religion", "Romance", "Science & Math", "Sci-Fi & Fantasy", "Self-Help", "Sports", "Teen", "Travel", "True Crime",
                "Westerns", "Offers", "Special Editions", "Website", "No Category"};
        List<Category> categories = new ArrayList<>();
        for (String categoryName : categoryNames)
        {
            Category category = new Category(categoryName);
            categories.add(category);
        }
        categoryDAO.saveAll(categories);
    }

    public List<Category> categories()
    {
        return categoryDAO.findAll();
    }

    public void addCategory(String name)
    {
        Category category = categoryDAO.findByName(name);
        if (category == null)
            categoryDAO.save(new Category(name));
    }

    public void removeCategory(int id)
    {
        Category category = categoryDAO.findById(id).get();
        Category noCategory = categoryDAO.findByName("No Category");
        Set<Book> books = category.getBooks();
        for (Book book : books)
        {
            Set<Category> categories = book.getCategories();
            for (Category category1 : categories)
            {
                if (category1.getName().equals(category.getName()))
                {
                    categories.remove(category1);
                    break;
                }
            }
            if (categories.size() == 0)
                categories.add(noCategory);
        }
        bookDAO.saveAll(books);
        categoryDAO.deleteById(id);
    }

    public void updateCategory(int id, String name)
    {
        Optional<Category> optional = categoryDAO.findById(id);
        if (optional.isPresent())
        {
            Category category = optional.get();
            category.setName(name);
            categoryDAO.save(category);
        }
    }
}
