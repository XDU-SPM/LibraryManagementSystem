package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * If we want to identify this table by id_name,
 * we can use "bkid".
 */
@Entity
@Table(name = "book_")
public class Book
{
    @Id
    private String isbn;
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "bkid")
    private Set<UserBkunit> userBkunits;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "bkid"),
            inverseJoinColumns = @JoinColumn(name = "aid")
    )
    private Set<Author> authors;

    private String category;

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }

    public Set<UserBkunit> getUserBkunits()
    {
        return userBkunits;
    }

    public void setUserBkunits(Set<UserBkunit> userBkunits)
    {
        this.userBkunits = userBkunits;
    }

    public Set<Author> getAuthors()
    {
        return authors;
    }

    public void setAuthors(Set<Author> authors)
    {
        this.authors = authors;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }
}
