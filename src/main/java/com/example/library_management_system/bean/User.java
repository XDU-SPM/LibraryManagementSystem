package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String name;
    private String email;

    private double money;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private Set<UserBkunit> userBkunits;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private Set<Review> reviews;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid")
    )
    private Set<Role> roles;

    // 可借数目上限 (Borrow Upper limit)
    private int BUL;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private Set<UserFavoriteBook> userFavoriteBooks;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "uid")
    private Set<Account> accounts;

    public User()
    {
        this.userBkunits = new HashSet<>();
        this.roles = new HashSet<>();
        this.userFavoriteBooks = new HashSet<>();
        this.accounts = new HashSet<>();
        this.reviews = new HashSet<>();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<UserBkunit> getUserBkunits()
    {
        return userBkunits;
    }


    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public double getMoney()
    {
        return money;
    }

    public void setMoney(double money)
    {
        this.money = money;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public int getBUL() {
        return BUL;
    }

    public Set<UserFavoriteBook> getUserFavoriteBooks() {
        return userFavoriteBooks;
    }

    public void setUserBkunits(Set<UserBkunit> userBkunits)
    {
        this.userBkunits = userBkunits;
    }

    public void setBUL(int BUL) {
        this.BUL = BUL;
    }


    public void setUserFavoriteBooks(Set<UserFavoriteBook> userFavoriteBooks)
    {
        this.userFavoriteBooks = userFavoriteBooks;
    }

    public void deductMoney(int money)
    {
        this.money -= money;
    }

    public Set<Account> getAccounts()
    {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts)
    {
        this.accounts = accounts;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }
}
