package com.example.library_management_system.service;

import com.example.library_management_system.bean.Location;
import com.example.library_management_system.dao.LocationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void addLocation(String name)
    {
        Location location = locationDAO.findByName(name);
        if (location == null)
            locationDAO.save(new Location(name));
    }

    public void removeLocation(int id)
    {
        locationDAO.deleteById(id);
    }

    public void updateLocation(int id, String name)
    {
        Optional<Location> optional = locationDAO.findById(id);
        if (optional.isPresent())
        {
            Location location = optional.get();
            location.setName(name);
        }
    }
}
