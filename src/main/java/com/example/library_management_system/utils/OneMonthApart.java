package com.example.library_management_system.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OneMonthApart
{
    private Date before;
    private Date after;

    public OneMonthApart()
    {
        before = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        getBeginDay(calendar);
        before = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        after = calendar.getTime();
    }

    public static void getBeginDay(Calendar calendar)
    {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void setLastMonth()
    {
        after = before;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        calendar.add(Calendar.MONTH, -1);
        before = calendar.getTime();
    }

    public Date getBefore()
    {
        return before;
    }

    public Date getAfter()
    {
        return after;
    }

    public static void main(String[] args)
    {
        OneMonthApart oneMonthApart = new OneMonthApart();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.println(sdf.format(oneMonthApart.getBefore()));
        System.out.println(sdf.format(oneMonthApart.getAfter()));
        oneMonthApart.setLastMonth();
        System.out.println(sdf.format(oneMonthApart.getBefore()));
        System.out.println(sdf.format(oneMonthApart.getAfter()));
    }
}
