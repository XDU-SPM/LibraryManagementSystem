package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "return_history")
public class ReturnHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private String bkunitId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publishDate;

    private double money;

    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    public ReturnHistory()
    {
    }

    public ReturnHistory(int userId, UserBkunit userBkunit, double money)
    {
        Bkunit bkunit = userBkunit.getBkunit();
        Book book = bkunit.getBook();
        this.userId = userId;
        this.bkunitId = bkunit.getId();
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publishDate = book.getPublishDate();
        this.publisher = book.getPublisher();
        this.borrowDate = userBkunit.getBorrowDate();
        this.returnDate = userBkunit.getReturnDate();
        this.money = money;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getBkunitId()
    {
        return bkunitId;
    }

    public void setBkunitId(String bkunitId)
    {
        this.bkunitId = bkunitId;
    }

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

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public String getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(String publishDate)
    {
        this.publishDate = publishDate;
    }

    public Date getBorrowDate()
    {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate)
    {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate()
    {
        return returnDate;
    }

    public void setReturnDate(Date returnDate)
    {
        this.returnDate = returnDate;
    }

    public double getMoney()
    {
        return money;
    }

    public void setMoney(double money)
    {
        this.money = money;
    }
}
