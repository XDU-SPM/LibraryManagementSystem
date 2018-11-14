package com.example.library_management_system;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.service.*;
import com.example.library_management_system.utils.RoleUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitTests
{
    @Autowired
    private RoleService roleService;

    @Autowired
    private GlobalUtilService globalUtilService;

    @Autowired
    private PunishmentService punishmentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads()
    {
        roleService.addRoleService();
        globalUtilService.initGlobalUtil();
        punishmentService.init();
        categoryService.init();
        locationService.init();

        User user = new User("admin", "admin");
        userService.registerService(user, RoleUtil.ROLE_ADMIN);
    }
}
