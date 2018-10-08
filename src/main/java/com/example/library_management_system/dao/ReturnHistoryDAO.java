package com.example.library_management_system.dao;

import com.example.library_management_system.bean.ReturnHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnHistoryDAO extends JpaRepository<ReturnHistory, Integer>
{
    Page<ReturnHistory> findAllByUserId(int id, Pageable pageable);
}
