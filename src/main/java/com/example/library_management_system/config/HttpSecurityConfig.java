package com.example.library_management_system.config;

import com.example.library_management_system.service.AdminUserService;
import com.example.library_management_system.service.StudentUserService;
import com.example.library_management_system.utils.MD5Util;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class HttpSecurityConfig
{
    @Configuration
    @Order(2)
    public static class StudentConfigurationAdapter extends WebSecurityConfigurerAdapter
    {
        @Autowired
        private StudentUserService studentUserService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception
        {
            auth.userDetailsService(studentUserService).passwordEncoder(new PasswordEncoder()
            {
                @Override
                public String encode(CharSequence charSequence)
                {
                    return MD5Util.encode((String) charSequence);
                }

                @Override
                public boolean matches(CharSequence charSequence, String s)
                {
                    return s.equals(MD5Util.encode((String) charSequence));
                }
            });
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.authorizeRequests()
                    .antMatchers("/student/register", "/student/login").permitAll()
                    .antMatchers("/student/**").hasRole(RoleUtil.STUDENT)
                    .and()
                    .formLogin()
                    .loginPage("/student/login")
                    .defaultSuccessUrl("/student/home")
                    .failureUrl("/student/login/error").permitAll()
                    .and()
                    .csrf().disable();
        }
    }

    @Configuration
    @Order(1)
    public static class AdminConfigurationAdapter extends WebSecurityConfigurerAdapter
    {
        @Autowired
        private AdminUserService adminUserService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception
        {
            auth.userDetailsService(adminUserService).passwordEncoder(new PasswordEncoder()
            {
                @Override
                public String encode(CharSequence charSequence)
                {
                    return MD5Util.encode((String) charSequence);
                }

                @Override
                public boolean matches(CharSequence charSequence, String s)
                {
                    return s.equals(MD5Util.encode((String) charSequence));
                }
            });
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.authorizeRequests()
                    .antMatchers("/public/**").permitAll()
                    .antMatchers("/admin/register", "/admin/login").permitAll()
                    .antMatchers("/admin/**").hasRole(RoleUtil.ADMIN)
                    .and()
                    .formLogin()
                    .loginPage("/admin/login")
                    .defaultSuccessUrl("/admin/home")
                    .failureUrl("/admin/login/error").permitAll()
                    .and()
                    .csrf().disable();
        }
    }
}
