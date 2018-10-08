package com.example.library_management_system.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * If we want to identify this table by id_name,
 * we can use "bkid".
 */
@Entity
@Table(name = "book_")
public class Book
{
    @Id
    private String isbn;
    private String title;

    private double score;

    @Lob
    private String brief;

    private int hardCover;  // pages

    private String publishDate;
    private String publisher;

    private String author;

    private double price;

    private String position;
    private String coverPath;

    private int frequency;

    @Transient
    private long number;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "bkid"),
            inverseJoinColumns = @JoinColumn(name = "cid")
    )
    private Set<Category> categories;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    @JsonIgnore
    private Set<UserFavoriteBook> userFavoriteBooks;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    private Set<Review> reviews;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    @JsonIgnore
    private Set<Bkunit> bkunits;

    public Book()
    {
        this.frequency = 0;
        this.categories = new HashSet<>();
        this.userFavoriteBooks = new HashSet<>();
        this.reviews = new HashSet<>();
        this.bkunits = new HashSet<>();
    }

    public Book(String isbn)
    {
        this();
        this.isbn = isbn;
    }

    public Book(String isbn, String title, double score, String publishDate, String publisher, String author, double price)
    {
        this(isbn, title, score, 0, publishDate, publisher, author, price);
    }

    public Book(String isbn, String title, double score, int hardCover, String publishDate, String publisher, String author, double price)
    {
        this();
        this.isbn = isbn;
        this.title = title;
        this.score = score;
        this.hardCover = hardCover;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.author = author;
        this.price = price;
    }

    public long getNumber()
    {
        return number;
    }

    public void setNumber(long number)
    {
        this.number = number;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    public void addFrequency()
    {
        this.frequency++;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(String publishDate)
    {
        this.publishDate = publishDate;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public Set<Category> getCategories()
    {
        return categories;
    }

    public void setCategories(Set<Category> categories)
    {
        this.categories = categories;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public String getBrief()
    {
        return brief;
    }

    public void setBrief(String brief)
    {
        this.brief = brief;
    }

    public int getHardCover()
    {
        return hardCover;
    }

    public void setHardCover(int hardCover)
    {
        this.hardCover = hardCover;
    }

    public Set<UserFavoriteBook> getUserFavoriteBooks()
    {
        return userFavoriteBooks;
    }

    public void setUserFavoriteBooks(Set<UserFavoriteBook> userFavoriteBooks)
    {
        this.userFavoriteBooks = userFavoriteBooks;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Bkunit> getBkunits()
    {
        return bkunits;
    }

    public void setBkunits(Set<Bkunit> bkunits)
    {
        this.bkunits = bkunits;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getCoverPath()
    {
        return coverPath;
    }

    public void setCoverPath(String coverPath)
    {
        this.coverPath = coverPath;
    }
}
