package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_bkunit")
public class UserBkunit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowDate;
    private Date returnDate;

    private int days = 30;   // 可借图书时长
    private int state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buid")
    private Bkunit bkunit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private User user;


    public UserBkunit(Date borrowDate, int days, int state, Bkunit bkunit, User user) {
        this.borrowDate = borrowDate;
        this.days = days;
        this.state = state;
        this.bkunit = bkunit;
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

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public Bkunit getBkunit() {
        return bkunit;
    }

    public void setBkunit(Bkunit bkunit) {
        this.bkunit = bkunit;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public int getDays()
    {
        return days;
    }

    public void setDays(int days)
    {
        this.days = days;
    }
}
