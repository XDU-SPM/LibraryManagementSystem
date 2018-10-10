package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account_")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int type;
    private int uid;

    private double money;

    private String bkid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Account()
    {
    }


    public Account(int type, int uid, double money, String bkid, Date date)
    {
        this.type = type;
        this.uid = uid;
        this.money = money;
        this.bkid = bkid;
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public double getMoney()
    {
        return money;
    }

    public void setMoney(double money)
    {
        this.money = money;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getUid()
    {
        return uid;
    }

    public void setUid(int uid)
    {
        this.uid = uid;
    }

    public String getBkid()
    {
        return bkid;
    }

    public void setBkid(String bkid)
    {
        this.bkid = bkid;
    }
}
