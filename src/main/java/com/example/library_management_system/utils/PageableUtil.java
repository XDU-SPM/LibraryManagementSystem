package com.example.library_management_system.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;

public class PageableUtil
{
    public static Pageable pageable(boolean asc, String field, int start, int size)
    {
        start = start < 0 ? 0 : start;
        Sort sort;
        if (asc)
            sort = new Sort(Sort.Direction.ASC, field);
        else
            sort = new Sort(Sort.Direction.DESC, field);
        return PageRequest.of(start, size, sort);
    }
}
