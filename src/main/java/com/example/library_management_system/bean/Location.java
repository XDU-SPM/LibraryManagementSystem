package com.example.library_management_system.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "location_")
public class Location
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "locations")
    @JsonIgnore
    private Set<Book> books;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "location")
    @JsonIgnore
    private Set<Bkunit> bkunits;

    public Location()
    {
        this.books = new HashSet<>();
        this.bkunits = new HashSet<>();
    }

    public Location(String name)
    {
        this();
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<Book> getBooks()
    {
        return books;
    }

    public void setBooks(Set<Book> books)
    {
        this.books = books;
    }

    public Set<Bkunit> getBkunits()
    {
        return bkunits;
    }

    public void setBkunits(Set<Bkunit> bkunits)
    {
        this.bkunits = bkunits;
    }
}
