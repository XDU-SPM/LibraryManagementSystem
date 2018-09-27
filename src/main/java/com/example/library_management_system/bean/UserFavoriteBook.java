package com.example.library_management_system.bean;

import javax.persistence.*;

/**
 * @ author Captain
 * @ date 2018/9/26
 * @ description as bellow.
 * 读者(用户)的收藏夹列表
 */

@Entity
@Table(name = "user_favoriteBook")
public class UserFavoriteBook
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "isbn")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private User user;

    public UserFavoriteBook()
    {
    }

    public UserFavoriteBook(Book book, User user)
    {
        this.book = book;
        this.user = user;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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
}

