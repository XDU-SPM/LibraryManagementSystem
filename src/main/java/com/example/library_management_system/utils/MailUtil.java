package com.example.library_management_system.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;

public class MailUtil extends Authenticator
{
    public static void sendmail(String to, String context, String subject) throws MessagingException, UnsupportedEncodingException
    {
        final String from = "ericfbt@163.com";//发件人的邮箱
        final String password = "admin123";
        final String host = "smtp.163.com";
        Properties properties = System.getProperties();//获取系统属性
        properties.setProperty("mail.transport.protocol", "smtp");//使用协议
        properties.setProperty("mail.smtp.host", host);//请求认证
        properties.setProperty("mail.smtp.port", "465");//ssl端口
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.auth", "true");
        MyAuthenticator ma = new MyAuthenticator(from, password);
        Session session = Session.getDefaultInstance(properties, ma);//获取邮件服务器
        session.setDebug(true);
        try
        {
            MimeMessage message = createMimeMessage(session, from, to, subject, context);
            Transport transport = session.getTransport();//根据session获得邮件传输对象
            transport.connect(from, password);
            transport.send(message, message.getAllRecipients());
            transport.close();
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }

    }

    private static MimeMessage createMimeMessage(Session session, String sendAccount, String receiveAccount, String subject, String context) throws MessagingException, UnsupportedEncodingException
    {
        MimeMessage mime = new MimeMessage(session);
        mime.setFrom(new InternetAddress(sendAccount));
        mime.setRecipient(RecipientType.TO, new InternetAddress(receiveAccount, "hello", "UTF-8"));
        mime.setSubject(subject, "UTF-8");
        mime.setContent(context, "text/html; charset=UTF-8");
        mime.setSentDate(new Date());
        mime.saveChanges();
        return mime;
    }

    public static void main(String[] args)
    {
        try
        {
            MailUtil.sendmail("ericfbt@foxmail.com", "context", "subject");
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
}

class MyAuthenticator extends javax.mail.Authenticator
{
    private String strUser;
    private String strPwd;

    public MyAuthenticator(String user, String password)
    {
        this.strUser = user;
        this.strPwd = password;
    }

    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(strUser, strPwd);
    }
}




