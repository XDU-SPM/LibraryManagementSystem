package com.example.library_management_system.dao;

import com.example.library_management_system.bean.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDAO extends JpaRepository<Message, Integer>
{
}
