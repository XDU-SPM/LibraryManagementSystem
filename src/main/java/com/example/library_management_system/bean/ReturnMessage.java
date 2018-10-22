package com.example.library_management_system.bean;

public class ReturnMessage
{
    private double overdueMoney;
    private double damagePercentage;
    private double bkunitPrice;

    public ReturnMessage()
    {
    }

    public ReturnMessage(double overdueMoney, double damagePercentage, double bkunitPrice)
    {
        this.overdueMoney = overdueMoney;
        this.damagePercentage = damagePercentage;
        this.bkunitPrice = bkunitPrice;
    }

    public double getOverdueMoney()
    {
        return overdueMoney;
    }

    public void setOverdueMoney(double overdueMoney)
    {
        this.overdueMoney = overdueMoney;
    }

    public double getDamagePercentage()
    {
        return damagePercentage;
    }

    public void setDamagePercentage(double damagePercentage)
    {
        this.damagePercentage = damagePercentage;
    }

    public double getBkunitPrice()
    {
        return bkunitPrice;
    }

    public void setBkunitPrice(double bkunitPrice)
    {
        this.bkunitPrice = bkunitPrice;
    }
}
