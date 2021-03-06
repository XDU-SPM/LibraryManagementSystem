package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bkuint_operating_history")
public class BkunitOperatingHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private int uid;    // 谁操作的

    @Transient
    private String username;

    private String buid;   // 操作的的哪本书

    @Transient
    private String bookName;

    private int status;

    @Transient
    private int status1;

    public BkunitOperatingHistory()
    {
    }

    public BkunitOperatingHistory(Date date, int uid, String buid, int status)
    {
        this.date = date;
        this.uid = uid;
        this.buid = buid;
        this.status = status;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public String getBuid()
    {
        return buid;
    }

    public void setBuid(String buid)
    {
        this.buid = buid;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }

    public int getStatus1()
    {
        return status1;
    }

    public void setStatus1(int status1)
    {
        this.status1 = status1;
    }
}
