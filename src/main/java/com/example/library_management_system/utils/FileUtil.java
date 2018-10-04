package com.example.library_management_system.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil
{
    public static void saveFile(MultipartFile file, File newFile)
    {
        try
        {
            byte[] bytes = file.getBytes();
            if (!newFile.exists())
                newFile.createNewFile();
            Files.write(Paths.get(newFile.getPath()), bytes);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
