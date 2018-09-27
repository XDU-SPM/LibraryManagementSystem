package com.example.library_management_system.utils;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Date;

//判断天数
public class OverduetimeUtil {
    public static int getoverduetime(Date startime,Date endtime){
        return (int) (endtime.getTime()-startime.getTime())/(24*60*60*1000);
    }
}
