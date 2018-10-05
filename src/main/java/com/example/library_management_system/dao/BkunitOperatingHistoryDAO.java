package com.example.library_management_system.dao;

import com.example.library_management_system.bean.BkunitOperatingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BkunitOperatingHistoryDAO extends JpaRepository<BkunitOperatingHistory, Integer>
{
    long countByDateBetweenAndStatus(Date before, Date after, int status);
}
