package com.example.library_management_system.bean;

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
//    private String brief;

    private int hardCover;  // pages

    private String publishDate;
    private String publisher;

    private String author;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "bkid"),
            inverseJoinColumns = @JoinColumn(name = "cid")
    )
    private Set<Category> categories;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "isbn")
    private Set<UserFavoriteBook> userFavoriteBooks;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "review")
    private Set<Review> reviews;

    public Book()
    {
        this.categories = new HashSet<>();
        this.userFavoriteBooks = new HashSet<>();
    }

    public Book(String isbn)
    {
        this();
        this.isbn = isbn;
    }

    public Book(String isbn, String title, double score, String publishDate, String publisher, String author)
    {
        this(isbn, title, score, 0, publishDate, publisher, author);
    }

    public Book(String isbn, String title, double score, int hardCover, String publishDate, String publisher, String author)
    {
        this();
        this.isbn = isbn;
        this.title = title;
        this.score = score;
        this.hardCover = hardCover;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.author = author;
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

//    public String getBrief()
//    {
//        return brief;
//    }
//
//    public void setBrief(String brief)
//    {
//        this.brief = brief;
//    }

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
}
