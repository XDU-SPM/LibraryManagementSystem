package com.example.library_management_system.bean;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bkid")
    private Book book;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "buid")
    private Set<UserBkunit> userBkunits;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

}
