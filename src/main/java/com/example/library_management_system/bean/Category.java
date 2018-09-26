package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category_")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "cid"),
            inverseJoinColumns = @JoinColumn(name = "bkid")
    )
    private Set<Book> books;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<Book> getBooks()
    {
        return books;
    }

    public void setBooks(Set<Book> books)
    {
        this.books = books;
    }
}
