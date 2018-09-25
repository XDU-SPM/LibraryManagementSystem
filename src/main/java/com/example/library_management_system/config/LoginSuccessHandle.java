package com.example.library_management_system.config;

import com.example.library_management_system.utils.RoleUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandle implements AuthenticationSuccessHandler
{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException
    {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String basePath = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort();
        if (roles.contains(RoleUtil.ROLE_ADMIN))
            httpServletResponse.sendRedirect(basePath + "/admin/home");
        else if (roles.contains(RoleUtil.ROLE_LIBRARIAN))
            httpServletResponse.sendRedirect(basePath + "/librarian/home");
        else
            httpServletResponse.sendRedirect(basePath + "/reader/home");
    }
}
