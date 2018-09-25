package com.example.library_management_system.dao;

import com.example.library_management_system.bean.UserBkunit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserBkunitDAO extends JpaRepository<UserBkunit, Integer>
{
    long countByDateBetweenAndState(Date before, Date after, int state);
    long countByState(int state);
    List<UserBkunit> findAllByState(int state);
}
