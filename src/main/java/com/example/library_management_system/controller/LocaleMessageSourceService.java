package com.example.library_management_system.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

@Component
public class LocaleMessageSourceService
{
    @Resource
    private MessageSource messageSource;

    public String getMessage(String code)
    {
        return getMessage(code, null);
    }

    public String getMessage(String code, Object[] args)
    {
        return getMessage(code, args, "");
    }

    public String getMessage(String code, Object[] args, String defaultMessage)
    {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
