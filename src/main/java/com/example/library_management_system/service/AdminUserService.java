package com.example.library_management_system.service;

import com.example.library_management_system.bean.Admin;
import com.example.library_management_system.bean.Role;
import com.example.library_management_system.dao.AdminDAO;
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
public class AdminUserService implements UserDetailsService
{
    @Autowired
    private AdminDAO adminDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        Admin admin = adminDAO.findByUsername(s);
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = admin.getRoles();
        for (Role role : roles)
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        return new User(s, admin.getPassword(), authorities);
    }
}