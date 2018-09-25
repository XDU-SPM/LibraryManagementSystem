package com.example.library_management_system.bean;

import javax.persistence.*;

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
}
