package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Bkunit;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.bean.UserBkunit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface UserBkunitDAO extends JpaRepository<UserBkunit, Integer>
{
    List<UserBkunit> findAllByStateOrState(int state1, int state2);

    List<UserBkunit> findAllByState(int state);

    Page<UserBkunit> findAllByUser(User reader, Pageable pageable);

    int countByUserAndBorrowDateBetween(User user, Date before, Date after);

    int countByUserAndReturnDateBetween(User user, Date before, Date after);

    UserBkunit findById(int id);

    UserBkunit findByUserAndBkunit(User user, Bkunit bkunit);

}
