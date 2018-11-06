package com.example.library_management_system.utils;

import com.example.library_management_system.bean.Role;

import java.util.Set;

public class RoleUtil
{
    public final static String
            READER = "READER",
            ADMIN = "ADMIN",
            LIBRARIAN = "LIBRARIAN",
            READER_CHECK = "READER_CHECK",
            LIBRARIAN_CHECK = "LIBRARIAN_CHECK",
            ROLE_READER = "ROLE_READER",
            ROLE_ADMIN = "ROLE_ADMIN",
            ROLE_LIBRARIAN = "ROLE_LIBRARIAN",
            ROLE_READER_CHECK = "ROLE_READER_CHECK",
            ROLE_LIBRARIAN_CHECK = "ROLE_LIBRARIAN_CHECK";

    public static boolean roleContainsString(Set<Role> roles, String string)
    {
        for (Role role : roles)
        {
            if (string.equals(role.getName()))
                return true;
        }
        return false;
    }
}
