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

        System.out.println("get " + id);

        UserBkunit userBkunit = userBkunitDAO.findById(id);
        if (userBkunit.getStatus() != UserBkunitUtil.RESERVATION)
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

        Date overdue = userBkunit.getReturnDate();
        if (now.before(overdue))
        {
            System.out.println("This delay had been canceled");
            return;
        }

        userBkunit.getBkunit().setStatus(BkunitUtil.NORMAL);
        userBkunit.setStatus(UserBkunitUtil.RESERVATION_FAIL);
        userBkunitDAO.save(userBkunit);
    }
}
