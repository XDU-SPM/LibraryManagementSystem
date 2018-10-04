package com.example.library_management_system.bean;

import java.util.Date;

public class MonthBorrow
{
    private Date date;
    private int num;

    public MonthBorrow()
    {
    }

    public MonthBorrow(Date date, int num)
    {
        this.date = date;
        this.num = num;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }
}
