package com.example.library_management_system.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String number;

    private String username;
    private String password;
    private String name;
    private String email;
    private String avatarPath;

    private double money;

    private int notifyDay;

    private double deposit;

    @Transient
    private String role;

    @Transient
    private double paidFine;

    @Transient
    private double unPaidFine;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<UserBkunit> userBkunits;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<UserBook> userBooks;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<Review> reviews;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid")
    )
    private Set<Role> roles;

    private int borrowNumber;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<UserFavoriteBook> userFavoriteBooks;

    public User()
    {
        this.userBkunits = new HashSet<>();
        this.roles = new HashSet<>();
        this.userFavoriteBooks = new HashSet<>();
        this.reviews = new HashSet<>();
        this.userBooks = new HashSet<>();
    }

    public User(String username, String password)
    {
        this();
        this.number = String.valueOf(System.currentTimeMillis());
        this.username = username;
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
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

    public Set<UserFavoriteBook> getUserFavoriteBooks() {
        return userFavoriteBooks;
    }

    public void setUserBkunits(Set<UserBkunit> userBkunits)
    {
        this.userBkunits = userBkunits;
    }

    public int getBorrowNumber()
    {
        return borrowNumber;
    }

    public void setBorrowNumber(int borrowNumber)
    {
        this.borrowNumber = borrowNumber;
    }

    public void setUserFavoriteBooks(Set<UserFavoriteBook> userFavoriteBooks)
    {
        this.userFavoriteBooks = userFavoriteBooks;
    }

    public void addMoney(double money)
    {
        this.money += money;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void addBookNumber(int number)
    {
        this.borrowNumber += number;
    }

    public Set<UserBook> getUserBooks()
    {
        return userBooks;
    }

    public void setUserBooks(Set<UserBook> userBooks)
    {
        this.userBooks = userBooks;
    }

    public String getAvatarPath()
    {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath)
    {
        this.avatarPath = avatarPath;
    }

    public double getPaidFine()
    {
        return paidFine;
    }

    public void setPaidFine(double paidFine)
    {
        this.paidFine = paidFine;
    }

    public double getUnPaidFine()
    {
        return unPaidFine;
    }

    public void setUnPaidFine(double unPaidFine)
    {
        this.unPaidFine = unPaidFine;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public int getNotifyDay()
    {
        return notifyDay;
    }

    public void setNotifyDay(int notifyDay)
    {
        this.notifyDay = notifyDay;
    }

    public double getDeposit()
    {
        return deposit;
    }

    public void setDeposit(double deposit)
    {
        this.deposit = deposit;
    }
}
