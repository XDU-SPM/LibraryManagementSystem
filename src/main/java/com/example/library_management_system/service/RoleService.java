package com.example.library_management_system.service;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.dao.RoleDAO;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{
    @Autowired
    private RoleDAO roleDAO;

    public void addRoleService()
    {
        Role readerRole = new Role(RoleUtil.ROLE_READER);
        roleDAO.save(readerRole);
        Role adminRole = new Role(RoleUtil.ROLE_ADMIN);
        roleDAO.save(adminRole);
        Role librarianRole = new Role(RoleUtil.ROLE_LIBRARIAN);
        roleDAO.save(librarianRole);
        Role readerCheckRole = new Role(RoleUtil.ROLE_READER_CHECK);
        roleDAO.save(readerCheckRole);
        Role librarianCheckRole = new Role(RoleUtil.ROLE_LIBRARIAN_CHECK);
        roleDAO.save(librarianCheckRole);
    }
}
