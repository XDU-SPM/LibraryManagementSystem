package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface AccountDAO extends JpaRepository<Account, Integer>
{
    Account findByDateBetweenAndType(Date before, Date after, int type);
}
