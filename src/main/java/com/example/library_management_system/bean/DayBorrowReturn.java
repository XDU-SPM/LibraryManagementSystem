package com.example.library_management_system.bean;

import java.util.Date;

public class DayBorrowReturn
{
    private Date date;
    private long borrowNumber;
    private long returnNumber;

    public DayBorrowReturn()
    {
    }

    public DayBorrowReturn(Date date, long borrowNumber, long returnNumber)
    {
        this.date = date;
        this.borrowNumber = borrowNumber;
        this.returnNumber = returnNumber;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public long getBorrowNumber()
    {
        return borrowNumber;
    }

    public void setBorrowNumber(long borrowNumber)
    {
        this.borrowNumber = borrowNumber;
    }

    public long getReturnNumber()
    {
        return returnNumber;
    }

    public void setReturnNumber(long returnNumber)
    {
        this.returnNumber = returnNumber;
    }
}
