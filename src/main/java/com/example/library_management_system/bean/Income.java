package com.example.library_management_system.bean;

import java.util.Date;

public class Income
{
    private double fine;
    private double deposit;
    private Date date;

    public Income()
    {
    }

    public Income(double fine, double deposit, Date date)
    {
        this.fine = fine;
        this.deposit = deposit;
        this.date = date;
    }

    public double getFine()
    {
        return fine;
    }

    public void setFine(double fine)
    {
        this.fine = fine;
    }

    public double getDeposit()
    {
        return deposit;
    }

    public void setDeposit(double deposit)
    {
        this.deposit = deposit;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
