package com.example.library_management_system.config;

import com.example.library_management_system.service.CustomUserService;
import com.example.library_management_system.utils.MD5Util;
import com.example.library_management_system.utils.RoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private LoginSuccessHandle loginSuccessHandle;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(customUserService).passwordEncoder(new PasswordEncoder()
        {
            @Override
            public String encode(CharSequence rawPassword)
            {
                return MD5Util.encode((String) rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword)
            {
                return encodedPassword.equals(MD5Util.encode((String) rawPassword));
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/dist/**").permitAll()
                .antMatchers("/login", "/register").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/reader/register", "/admin/register", "/librarian/register").permitAll()
                .antMatchers("/readerHome").hasRole(RoleUtil.READER_CHECK)
                .antMatchers("/librarianHome").hasRole(RoleUtil.LIBRARIAN_CHECK)
                .antMatchers("/reader/**").hasRole(RoleUtil.READER)
                .antMatchers("/admin/**").hasRole(RoleUtil.ADMIN)
                .antMatchers("/librarian/**").hasRole(RoleUtil.LIBRARIAN)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginSuccessHandle)     // Show different homepages to different users
                .failureUrl("/login?error").permitAll()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/css/**", "/js/**");    // 设置不拦截规则（静态）
    }
}
