package com.example.library_management_system.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OneDayApart
{
    private Date before;
    private Date after;

    public OneDayApart()
    {
        before = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        getBeginDay(calendar);
        before = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        after = calendar.getTime();
    }

    public static void getBeginDay(Calendar calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        OneDayApart oneDayApart = new OneDayApart();
        System.out.println(sdf.format(oneDayApart.getBefore()));
        System.out.println(sdf.format(oneDayApart.getAfter()));
    }
}
