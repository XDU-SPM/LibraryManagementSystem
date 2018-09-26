package com.example.library_management_system.service;
import com.example.library_management_system.bean.*;
import com.example.library_management_system.dao.BkunitDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
public class LibrarianBookService {

    @Autowired
    private BkunitDAO bkunitdao;

    public boolean addBook(Bkunit bkunit)
    {
         bkunitdao.save(bkunit);
         return true;
    }

    public boolean deleteBook(Bkunit bkunit)
    {
        bkunitdao.deleteById(bkunit.getId());
        return true;

    }
    public  Page<Bkunit> showBook(int start,int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Bkunit> page = bkunitdao.findAll(pageable);
        return page;
    }

    public Bkunit searchbyid(String id)
    {
        return bkunitdao.findById(id).get();
    }

    public boolean changeinfo(Bkunit bkunit)
    {
        bkunitdao.save(bkunit);
        return true;
    }
}
