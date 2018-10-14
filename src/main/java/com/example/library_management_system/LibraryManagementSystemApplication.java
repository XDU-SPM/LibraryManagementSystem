package com.example.library_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class LibraryManagementSystemApplication
{
    @Bean
    public LocaleResolver localeResolver()
    {
        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(Locale.CHINA);
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(LibraryManagementSystemApplication.class, args);
    }
}
