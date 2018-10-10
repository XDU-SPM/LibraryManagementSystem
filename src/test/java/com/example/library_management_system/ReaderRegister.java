package com.example.library_management_system;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.UserService;
import com.example.library_management_system.utils.RoleUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReaderRegister
{
    @Autowired
    private UserService userService;

    @Test
    public void init()
    {
        User user = new User("reader", "reader");
        userService.registerService(user, RoleUtil.ROLE_READER_CHECK);
    }
}