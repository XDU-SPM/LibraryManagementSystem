package com.example.library_management_system.service;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.dao.RoleDAO;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService
{
    @Autowired
    private RoleDAO roleDAO;

    @Transactional
    public void addRoleService()
    {
        Role studentRole = new Role(RoleUtil.ROLE_STUDENT);
        roleDAO.save(studentRole);
        Role adminRole = new Role(RoleUtil.ROLE_ADMIN);
        roleDAO.save(adminRole);
    }
}
