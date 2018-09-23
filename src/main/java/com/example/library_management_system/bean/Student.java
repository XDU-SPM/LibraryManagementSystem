package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "student_")
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "sid")
    private Set<StudentBook> studentBooks;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_role",
            joinColumns = @JoinColumn(name = "sid"),
            inverseJoinColumns = @JoinColumn(name = "rid")
    )
    private Set<Role> roles;


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

    public Set<StudentBook> getStudentBooks()
    {
        return studentBooks;
    }

    public void setStudentBooks(Set<StudentBook> studentBooks)
    {
        this.studentBooks = studentBooks;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }
}
