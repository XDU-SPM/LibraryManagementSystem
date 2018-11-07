package com.example.library_management_system.bean;

import com.example.library_management_system.utils.BkunitUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * If we want to identify this table by id_name,
 * we can use "buid".
 */
@Entity
@Table(name = "bkunit_")
public class Bkunit
{
    @Id
    private String id;

    private int status;
    private int damageStatus;

    @Transient
    private String status1;

    private String position;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bkid")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "lid")
    private Location location;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bkunit")
    @JsonIgnore
    private Set<UserBkunit> userBkunits;

    public Bkunit(String id, Book book, Location location)
    {
        this();
        this.id = id;
        this.status = BkunitUtil.NORMAL;
        this.book = book;
        this.damageStatus = BkunitUtil.NO_DAMAGE;
        this.location = location;
    }

    public Bkunit()
    {
        this.userBkunits = new HashSet<>();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public Set<UserBkunit> getUserBkunits()
    {
        return userBkunits;
    }

    public void setUserBkunits(Set<UserBkunit> userBkunits)
    {
        this.userBkunits = userBkunits;
    }

    public int getDamageStatus()
    {
        return damageStatus;
    }

    public void setDamageStatus(int damageStatus)
    {
        this.damageStatus = damageStatus;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getStatus1()
    {
        return status1;
    }

    public void setStatus1(String status1)
    {
        this.status1 = status1;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }
}
