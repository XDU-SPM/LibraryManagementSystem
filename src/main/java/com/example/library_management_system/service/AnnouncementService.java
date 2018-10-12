package com.example.library_management_system.service;

import com.example.library_management_system.bean.Announcement;
import com.example.library_management_system.bean.User;
import com.example.library_management_system.dao.AnnouncementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService
{
    @Autowired
    private AnnouncementDAO announcementDAO;

    @Autowired
    private UserService userService;

    public List<Announcement> getAnnouncements()
    {
        return announcementDAO.findAll();
    }

    public void addAnnouncement(Announcement announcement)
    {
        User user = userService.getUser();
        announcement.setUsername(user.getUsername());
        announcement.setDate(new Date());
        announcementDAO.save(announcement);
    }
}
