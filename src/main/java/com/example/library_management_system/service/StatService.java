package com.example.library_management_system.service;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Service
public class StatService
{
    @Autowired
    private UserBkunitDAO userBkunitDAO;

    @Autowired
    private UserDAO userDAO;

    public int[] monthborrow(int[] book_month,int uid)
   {
       Calendar cal=Calendar.getInstance();
       int year=cal.get(Calendar.YEAR);
       for(int i=0;i<12;i++)
         {
             Date startdate=new Date();
             Date enddate=new Date();
             startdate.setYear(year-1900);
             startdate.setMonth(i);
             startdate.setDate(1);
             startdate.setHours(0);
             startdate.setMinutes(0);
             startdate.setSeconds(0);
             enddate.setYear(year-1900);
             enddate.setMonth(i);
             if(i==1||i==3||i==5||i==7||i==8||i==10||i==12)
             {
                 enddate.setDate(31);
             }
             else if(i==2)
             {
                 if(year % 4 == 0 && year % 100 != 0 ||year % 400 == 0)
                 {
                     enddate.setDate(29);
                 }
                 else
                 {
                     enddate.setDate(28);
                 }
             }
             else
             {
                      enddate.setTime(30);
             }
             enddate.setHours(0);
             enddate.setMinutes(0);
             enddate.setSeconds(0);
             User user=userDAO.findById(uid);
             book_month[i]= userBkunitDAO.countByUserAndBorrowDateBetween(user,startdate,enddate);
         }
        return book_month;
   }

}
