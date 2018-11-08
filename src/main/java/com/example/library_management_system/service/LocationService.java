package com.example.library_management_system.service;

import com.example.library_management_system.bean.Location;
import com.example.library_management_system.dao.LocationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService
{
    @Autowired
    private LocationDAO locationDAO;

    public void init()
    {
        List<Location> locations = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 1; k < 10; k++)
                {
                    Location location = new Location("" + (char)('A' + i) + "-" + j + "0" + k);
                    locations.add(location);
                }
            }
        }

        locationDAO.saveAll(locations);
    }

    public List<Location> locations()
    {
        return locationDAO.findAll();
    }
}
