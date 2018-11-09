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
    List<UserBkunit> findAllByStatusOrStatus(int status1, int status2);

    List<UserBkunit> findAllByStatus(int status);

    Page<UserBkunit> findAllByStatusBetween(int status1, int status2, Pageable pageable);

    Set<UserBkunit> findAllByStatusBetween(int status1, int status2);

    Set<UserBkunit> findAllByStatusBetweenOrReserveStatus(int status1, int status2, int reserveStatus);

    Page<UserBkunit> findAllByUser(User reader, Pageable pageable);

    Page<UserBkunit> findAllByUserAndStatusBetween(User user, int status1, int status2, Pageable pageable);

    Page<UserBkunit> findAllByUserAndStatus(User user, int status, Pageable pageable);

    int countByUserAndBorrowDateBetween(User user, Date before, Date after);

    int countByUserAndReturnDateBetween(User user, Date before, Date after);

    int countByUserAndStatus(User user, int status);

    List<UserBkunit> findAllByUserAndStatus(User user, int status);

    UserBkunit findById(int id);

    UserBkunit findByUserAndBkunit(User user, Bkunit bkunit);

    UserBkunit findByBkunitAndStatusBetween(Bkunit bkunit, int status1, int status2);

    UserBkunit findByBkunitAndStatus(Bkunit bkunit, int status);

    UserBkunit findByUserAndBkunitAndStatus(User user, Bkunit bkunit, int status);
}
