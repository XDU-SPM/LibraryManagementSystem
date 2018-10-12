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

    public Income(Date date)
    {
        this.fine = 0;
        this.deposit = 0;
        this.date = date;
    }

    public Income(double fine, double deposit, Date date)
    {
        this.fine = fine;
        this.deposit = deposit;
        this.date = date;
    }

    public void addFine(double money)
    {
        this.fine += money;
    }

    public void addDeposit(double money)
    {
        this.deposit += money;
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
