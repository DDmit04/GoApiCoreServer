package com.goapi.goapi.controller.user;

import com.goapi.goapi.controller.form.user.RefreshTokensForm;
import com.goapi.goapi.controller.form.user.UserAuthInfo;
import com.goapi.goapi.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {

    @Value("${jwt.token.name.accessCookieName}")
    private String accessTokenCookieName;
    private final UserService userService;

    @PostMapping("/refreshJwt")
    private ResponseEntity<RefreshTokensForm> refreshJwt(
        @RequestBody RefreshTokensForm refreshTokenForm,
        @CookieValue(name = "${jwt.token.name.accessCookieName}") String accessToken,
        HttpServletResponse response) {

        UserAuthInfo userInfo = userService.refreshTokens(accessToken, refreshTokenForm.getRefreshToken());
        response.setStatus(HttpStatus.OK.value());
        Cookie accessTokenCookie = new Cookie(accessTokenCookieName, userInfo.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        RefreshTokensForm body = new RefreshTokensForm(userInfo.getRefreshToken());
        return ResponseEntity.ok(body);
    }

}
