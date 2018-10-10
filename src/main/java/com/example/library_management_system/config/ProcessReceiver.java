package com.example.library_management_system.config;

import com.example.library_management_system.bean.UserBook;
import com.example.library_management_system.dao.UserBookDAO;
import com.example.library_management_system.utils.UserBookUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.Date;

@Component
public class ProcessReceiver implements ChannelAwareMessageListener
{
    @Autowired
    private UserBookDAO userBookDAO;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception
    {
        ByteArrayInputStream in = new ByteArrayInputStream(message.getBody());
        ObjectInputStream sIn = new ObjectInputStream(in);
        int id = (Integer) sIn.readObject();

        System.out.println("get " + id);

        UserBook userBook = userBookDAO.findById(id).get();
        if (userBook.getStatus() != UserBookUtil.RESERVATION)
        {
            System.out.println("This book is not reservation");
            return;
        }

        // will generate some bugs
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, 1);
        now = calendar.getTime();

        Date overdue = userBook.getReturnDate();
        if (now.before(overdue))
        {
            System.out.println("This delay had been canceled");
            return;
        }

        userBook.getBook().addNumber(1);
        userBook.setStatus(UserBookUtil.RESERVATION_FAIL);
        userBookDAO.save(userBook);
    }
}
