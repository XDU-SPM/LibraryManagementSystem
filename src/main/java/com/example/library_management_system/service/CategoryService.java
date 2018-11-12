package com.example.library_management_system.service;

import com.example.library_management_system.bean.Category;
import com.example.library_management_system.dao.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
    @Autowired
    private CategoryDAO categoryDAO;

    public void init()
    {
        String[] categoryNames = {"Literature & Fiction", "Arts & Music", "Biographies", "Business", "Kids", "Comics",
                "Computers & Tech", "Cooking", "Hobbies & Crafts", "Edu & Reference", "Gay & Lesbian", "Health & Fitness",
                "History", "Home & Garden", "Horror", "Entertainment", "Medical", "Mysteries", "Parenting", "Social Sciences",
                "Religion", "Romance", "Science & Math", "Sci-Fi & Fantasy", "Self-Help", "Sports", "Teen", "Travel", "True Crime",
                "Westerns", "Offers", "Special Editions", "Website"};
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
