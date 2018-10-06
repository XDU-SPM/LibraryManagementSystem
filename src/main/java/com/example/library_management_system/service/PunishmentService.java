package com.example.library_management_system.service;

import com.example.library_management_system.bean.Punishment;
import com.example.library_management_system.dao.PunishmentDAO;
import com.example.library_management_system.utils.BkunitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PunishmentService
{
    @Autowired
    private PunishmentDAO punishmentDAO;

    public void init()
    {
        List<Punishment> punishments = new ArrayList<>();
        punishments.add(new Punishment(BkunitUtil.NO_DAMAGE, 0));
        punishments.add(new Punishment(BkunitUtil.MILD_DAMAGE, 0.1));
        punishments.add(new Punishment(BkunitUtil.MODERATE_DAMAGE, 0.5));
        punishments.add(new Punishment(BkunitUtil.MAJOR_DAMAGE, 0.9));
        punishments.add(new Punishment(BkunitUtil.LOST, 1));
        punishmentDAO.saveAll(punishments);
    }
}
