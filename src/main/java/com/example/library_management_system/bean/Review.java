package com.example.library_management_system.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * @ author Captain
 * @ date 2018/9/27
 * @ description as bellow.
 */

@Entity
@Table(name = "review_")
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String review;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "isbn")
    @JsonIgnore
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "uid")
    private User user;

    public Review()
    {
    }

    public Review(String review, String title, Date date, Book book, User user)
    {
        this.review = review;
        this.title = title;
        this.date = date;
        this.book = book;
        this.user = user;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getReview()
    {
        return review;
    }

    public void setReview(String review)
    {
        this.review = review;
    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
