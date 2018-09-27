package com.example.library_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.library_management_system.dao.*;
import com.example.library_management_system.bean.*;
@Service
public class AdminService {

    @Autowired
    private UserDAO userdao;
    @Autowired
    private UserBkunitDAO userbkunitdao;
    @Autowired
    private UserFavoriteBookDAO userfavoritebookdao;

    public  void deleteuser(User user)
    {
        userdao.deleteById(user.getId());
        userbkunitdao.deleteById(user.getId());
        userfavoritebookdao.deleteById(user.getId());
    }

    public Page<User> showallinfo(int start,int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<User> page = userdao.findAll(pageable);
        return page;
    }

    public User showinfo(int id)
    {
        return userdao.findById(id).get();
    }

    public void changeinfo(User user)
    {
        userdao.save(user);
    }
}
