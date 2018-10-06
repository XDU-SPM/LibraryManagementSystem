package com.example.library_management_system.bean;

import javax.persistence.*;

/**
 * @ author Captain
 * @ date 2018/10/4
 * @ description as bellow.
 */

@Entity
@Table(name = "global_")
public class GlobalUtil
{
    @Id
    private int id;
    private double REGISTER_MONEY;
    private int MAX_BORROW_DAYS;
    private int MAX_BORROW_NUM;
    private int OVERDUE_MONEY;

    public GlobalUtil()
    {
        this.id = 1;
        this.REGISTER_MONEY = 300;
        this.MAX_BORROW_DAYS = 30;
        this.MAX_BORROW_NUM = 3;
        this.OVERDUE_MONEY = 1;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getREGISTER_MONEY()
    {
        return REGISTER_MONEY;
    }

    public void setREGISTER_MONEY(double REGISTER_MONEY)
    {
        this.REGISTER_MONEY = REGISTER_MONEY;
    }

    public int getMAX_BORROW_DAYS()
    {
        return MAX_BORROW_DAYS;
    }

    public void setMAX_BORROW_DAYS(int MAX_BORROW_DAYS)
    {
        this.MAX_BORROW_DAYS = MAX_BORROW_DAYS;
    }

    public int getMAX_BORROW_NUM()
    {
        return MAX_BORROW_NUM;
    }

    public void setMAX_BORROW_NUM(int MAX_BORROW_NUM)
    {
        this.MAX_BORROW_NUM = MAX_BORROW_NUM;
    }

    public int getOVERDUE_MONEY()
    {
        return OVERDUE_MONEY;
    }

    public void setOVERDUE_MONEY(int OVERDUE_MONEY)
    {
        this.OVERDUE_MONEY = OVERDUE_MONEY;
    }
}
