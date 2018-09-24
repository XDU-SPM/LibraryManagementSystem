package com.example.library_management_system.service;

import com.example.library_management_system.bean.Role;
import com.example.library_management_system.bean.Student;
import com.example.library_management_system.dao.RoleDAO;
import com.example.library_management_system.dao.StudentDAO;
import com.example.library_management_system.utils.MD5Util;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class StudentService
{
    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private RoleDAO roleDAO;

    public void registerService(Student student)
    {
        student.setPassword(MD5Util.encode(student.getPassword()));
        Role role = roleDAO.findByName(RoleUtil.ROLE_STUDENT);
        if (student.getRoles() == null)
            student.setRoles(new HashSet<>());
        student.getRoles().add(role);
        studentDAO.save(student);
    }
}
