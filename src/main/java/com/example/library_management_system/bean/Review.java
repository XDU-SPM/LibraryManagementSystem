package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * @ author Captain
 * @ date 2018/9/27
 * @ description as bellow.
 */


public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String review;
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "isbn")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private User user;

    public Review(String review, Date date, Book book, User user) {
        this.review = review;
        this.date = date;
        this.book = book;
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
