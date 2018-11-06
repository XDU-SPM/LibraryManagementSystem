package com.example.library_management_system.service;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.dao.UserDAO;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserService implements UserDetailsService
{
    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        String[] strings = s.split(" ");
        String type = "0";
        if (strings.length == 2)
            type = strings[1];
        User user = userDAO.findByUsername(strings[0]);
        if (user == null)
            throw new UsernameNotFoundException("233");
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roleSet = user.getRoles();
        if ("0".equals(type)
                || ("1".equals(type) && (RoleUtil.roleContainsString(roleSet, RoleUtil.ROLE_READER_CHECK) || RoleUtil.roleContainsString(roleSet, RoleUtil.ROLE_LIBRARIAN_CHECK)))
                || "2".equals(type) && RoleUtil.roleContainsString(roleSet, RoleUtil.ROLE_ADMIN))
        {
            for (Role role : roleSet)
                authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        else
            throw new UsernameNotFoundException("233");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
