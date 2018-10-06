package com.example.library_management_system.config;

import com.example.library_management_system.bean.UserBkunit;
import com.example.library_management_system.dao.UserBkunitDAO;
import com.example.library_management_system.utils.BkunitUtil;
import com.example.library_management_system.utils.UserBkunitUtil;
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
    private UserBkunitDAO userBkunitDAO;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception
    {
        ByteArrayInputStream in = new ByteArrayInputStream(message.getBody());
        ObjectInputStream sIn = new ObjectInputStream(in);
        int id = (Integer) sIn.readObject();
        UserBkunit userBkunit = userBkunitDAO.findById(id);
        if (userBkunit.getStatus() != UserBkunitUtil.RESERVATION)
            return;

        Calendar overdue = Calendar.getInstance();
        overdue.setTime(userBkunit.getBorrowDate());
        overdue.add(Calendar.MILLISECOND, QueueConfig.QUEUE_EXPIRATION);
        Date now = new Date();
        if (now.before(overdue.getTime()))
            return;

        userBkunit.getBkunit().setStatus(BkunitUtil.NORMAL);
        userBkunit.setStatus(UserBkunitUtil.RESERVATION_FAIL);
        userBkunitDAO.save(userBkunit);
    }
}
