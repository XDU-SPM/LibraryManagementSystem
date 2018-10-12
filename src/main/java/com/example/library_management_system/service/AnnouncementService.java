package com.example.library_management_system.service;

import com.example.library_management_system.bean.Announcement;
import com.example.library_management_system.dao.AnnouncementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService
{
    @Autowired
    private AnnouncementDAO announcementDAO;

    public List<Announcement> getAnnouncements()
    {
        return announcementDAO.findAll();
    }
}
