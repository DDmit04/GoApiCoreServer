package com.goapi.goapi.security.handler;

import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.user.UserJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Daniil Dmitrochenkov
 **/
@Component
@RequiredArgsConstructor
public class LogOutSuccessHandler extends SecurityContextLogoutHandler {

    private final UserJwtService userJwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userJwtService.invalidateJwtRefreshToken(user);
        super.logout(request, response, authentication);
    }
}
