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

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    private int status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "buid")
    private Bkunit bkunit;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "uid")
    private User user;

    public UserBkunit()
    {
    }

    public UserBkunit(Date borrowDate, Date returnDate, int status, Bkunit bkunit, User user)
    {
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
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

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Bkunit getBkunit()
    {
        return bkunit;
    }

    public void setBkunit(Bkunit bkunit)
    {
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
}
