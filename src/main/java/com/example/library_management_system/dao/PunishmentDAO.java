package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Punishment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PunishmentDAO extends JpaRepository<Punishment, Integer>
{
}
