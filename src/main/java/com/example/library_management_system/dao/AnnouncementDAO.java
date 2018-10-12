package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementDAO extends JpaRepository<Announcement, Integer>
{
}
