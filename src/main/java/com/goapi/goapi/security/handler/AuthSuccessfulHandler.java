package com.goapi.goapi.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.security.JwtUtils;
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

    @Value("${jwt.token.name.refreshFiledName}")
    private String refreshTokenFieldName;
    @Value("${jwt.token.name.accessCookieName}")
    private String accessTokenCookieName;
    private final JwtUtils jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getRefreshTokenUuid().toString());
        ObjectMapper mapper = new ObjectMapper();
        SimpleImmutableEntry<String, String> token = new SimpleImmutableEntry<>(refreshTokenFieldName, refreshToken);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(token));

        String jwt = jwtTokenUtil.generateAccessToken(user.getUsername());
        Cookie jwtTokenCookie = new Cookie(accessTokenCookieName, jwt);
        jwtTokenCookie.setHttpOnly(true);
        response.addCookie(jwtTokenCookie);
    }
}
