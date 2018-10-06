package com.example.library_management_system;

import com.example.library_management_system.service.GlobalUtilService;
import com.example.library_management_system.service.PunishmentService;
import com.example.library_management_system.service.RoleService;
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

    @Test
    public void contextLoads()
    {
        roleService.addRoleService();
        globalUtilService.initGlobalUtil();
        punishmentService.init();
    }
}
