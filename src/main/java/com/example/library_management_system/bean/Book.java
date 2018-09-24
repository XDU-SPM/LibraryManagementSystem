package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "book_")
public class Book
{
    @Id
    private String isbn;
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "bid")
    private Set<StudentBook> studentBooks;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "bid"),
            inverseJoinColumns = @JoinColumn(name = "aid")
    )
    private Set<Author> authors;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid")
    private Category category;

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }

    public Set<StudentBook> getStudentBooks()
    {
        return studentBooks;
    }

    public void setStudentBooks(Set<StudentBook> studentBooks)
    {
        this.studentBooks = studentBooks;
    }

    public Set<Author> getAuthors()
    {
        return authors;
    }

    public void setAuthors(Set<Author> authors)
    {
        this.authors = authors;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }
}
