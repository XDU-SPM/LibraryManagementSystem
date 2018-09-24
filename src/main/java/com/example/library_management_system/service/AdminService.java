package com.example.library_management_system.service;

import com.example.library_management_system.bean.Admin;
import com.example.library_management_system.bean.Role;
import com.example.library_management_system.dao.AdminDAO;
import com.example.library_management_system.dao.RoleDAO;
import com.example.library_management_system.utils.MD5Util;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class AdminService
{
    @Autowired
    private AdminDAO adminDAO;

    @Autowired
    private RoleDAO roleDAO;

    public void registerService(Admin admin)
    {
        admin.setPassword(MD5Util.encode(admin.getPassword()));
        Role role = roleDAO.findByName(RoleUtil.ROLE_ADMIN);
        if (admin.getRoles() == null)
            admin.setRoles(new HashSet<>());
        admin.getRoles().add(role);
        adminDAO.save(admin);
    }
}
