package com.example.library_management_system.dao;

import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBkunitDAO extends JpaRepository<UserBkunit, Integer>
{
    List<UserBkunit> findAllByState(int state);

    Page<UserBkunit> findAllByUser(User reader, Pageable pageable);

    UserBkunit findById(int id);
}
