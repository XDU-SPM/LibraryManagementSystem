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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bkid")
    private Book book;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bkunit")
    @JsonIgnore
    private Set<UserBkunit> userBkunits;

    public Bkunit(String id, Book book)
    {
        this();
        this.id = id;
        this.status = BkunitUtil.NORMAL;
        this.book = book;
        this.damageStatus = BkunitUtil.NO_DAMAGE;
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
}
