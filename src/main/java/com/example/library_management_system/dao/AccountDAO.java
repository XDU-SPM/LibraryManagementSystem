package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Set;

public interface AccountDAO extends JpaRepository<Account, Integer>
{
    Set<Account> findAllByDateBetween(Date before, Date after);

    Set<Account> findAllByUid(int uid);

    Set<Account> findAllByUidAndType(int uid, int type);

    Set<Account> findAllByUidAndBkidAndTypeAndDateBetween(int uid, String bkid, int type, Date before, Date after);

    Set<Account> findAllByUidAndBkidAndTypeBetween(int uid, String bkid, int type1, int type2);
}
