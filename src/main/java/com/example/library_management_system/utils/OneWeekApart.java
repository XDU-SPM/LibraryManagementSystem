package com.example.library_management_system.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OneWeekApart
{
    private Date before;
    private Date after;

    public OneWeekApart()
    {
        before = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        getBeginWeek(calendar);
        before = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        after = calendar.getTime();
    }

    public static void getBeginWeek(Calendar calendar)
    {
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void setLastWeek()
    {
        after = before;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
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
        OneWeekApart oneWeekApart = new OneWeekApart();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.println(sdf.format(oneWeekApart.getBefore()));
        System.out.println(sdf.format(oneWeekApart.getAfter()));
        oneWeekApart.setLastWeek();
        System.out.println(sdf.format(oneWeekApart.getBefore()));
        System.out.println(sdf.format(oneWeekApart.getAfter()));
    }
}
