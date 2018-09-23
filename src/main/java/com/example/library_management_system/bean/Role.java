package com.example.library_management_system.bean;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role_")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_role",
            joinColumns = @JoinColumn(name = "rid"),
            inverseJoinColumns = @JoinColumn(name = "sid")
    )
    private Set<Student> students;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "admin_role",
            joinColumns = @JoinColumn(name = "rid"),
            inverseJoinColumns = @JoinColumn(name = "aid")
    )
    private Set<Admin> admins;

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

    public Set<Student> getStudents()
    {
        return students;
    }

    public void setStudents(Set<Student> students)
    {
        this.students = students;
    }

    public Set<Admin> getAdmins()
    {
        return admins;
    }

    public void setAdmins(Set<Admin> admins)
    {
        this.admins = admins;
    }
}
