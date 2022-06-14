package com.goapi.goapi.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.service.interfaces.facade.user.UserServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * @author Daniil Dmitrochenkov
 **/
@Component
@RequiredArgsConstructor
public class AuthSuccessfulHandler implements AuthenticationSuccessHandler {

    @Value("${tokens.name.refresh}")
    private String refreshTokenCookieName;
    @Value("${tokens.name.access}")
    private String accessTokenCookieName;

    @Value("${urls.jwr-refresh.path.final}")
    private String refreshJwtUrl;
    private final JwtUtils jwtTokenUtil;

    private final UserServiceFacade userServiceFacade;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        user = userServiceFacade.addRefreshJwtToken(user);

        String jwtRefreshToken = user.getJwtRefreshToken();
        String refreshToken = jwtTokenUtil.generateRefreshToken(jwtRefreshToken);
        String accessToken = jwtTokenUtil.generateAccessToken(user.getUsername());

        ObjectMapper mapper = new ObjectMapper();
        SimpleImmutableEntry<String, String> token = new SimpleImmutableEntry<>(accessTokenCookieName, accessToken);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(token));

        Cookie jwtTokenCookie = new Cookie(refreshTokenCookieName, refreshToken);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setSecure(false);
        jwtTokenCookie.setPath(refreshJwtUrl);
        response.addCookie(jwtTokenCookie);
    }
}
