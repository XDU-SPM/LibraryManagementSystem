package com.example.library_management_system.service;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.Student;
import com.example.library_management_system.dao.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StudentUserService implements UserDetailsService
{
    @Autowired
    private StudentDAO studentDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        Student student = studentDAO.findByUsername(s);
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = student.getRoles();
        for (Role role : roles)
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        return new User(s, student.getPassword(), authorities);
    }
}
