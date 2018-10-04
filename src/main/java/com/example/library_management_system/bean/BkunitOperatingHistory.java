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
    private String buid;   // 操作的的哪本书

    private int state;

    public BkunitOperatingHistory()
    {
    }

    public BkunitOperatingHistory(Date date, int uid, String buid, int state)
    {
        this.date = date;
        this.uid = uid;
        this.buid = buid;
        this.state = state;
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

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }
}
