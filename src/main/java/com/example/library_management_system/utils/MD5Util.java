package com.example.library_management_system.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util
{
    private MD5Util() throws Exception
    {
        throw new Exception("Exception");
    }

    public static String encode(String str)
    {
        MessageDigest md5;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte)charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int)md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args)
    {
        System.out.println(encode("student"));
    }
}
