package com.goapi.goapi.security.handler;

import com.goapi.goapi.service.interfaces.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        userService.invalidateJwtRefreshToken(userDetails.getUsername());
        super.logout(request, response, authentication);
    }
}
