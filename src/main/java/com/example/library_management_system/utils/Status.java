package com.example.library_management_system.utils;

public class Status
{
    private int status;
    private int number;

    public Status()
    {
    }

    public Status(int status)
    {
        this.status = status;
    }

    public Status(int status, int number)
    {
        this.status = status;
        this.number = number;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }
}
