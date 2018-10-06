package com.example.library_management_system.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "punishment_")
public class Punishment
{
    @Id
    private int id;
    private double rate;

    public Punishment()
    {
    }

    public Punishment(int id, double rate)
    {
        this.id = id;
        this.rate = rate;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getRate()
    {
        return rate;
    }

    public void setRate(double rate)
    {
        this.rate = rate;
    }
}
