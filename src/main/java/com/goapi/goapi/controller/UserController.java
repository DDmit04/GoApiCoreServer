package com.goapi.goapi.controller;

import com.goapi.goapi.controller.form.user.RefreshTokensForm;
import com.goapi.goapi.controller.form.user.UserAuthInfo;
import com.goapi.goapi.controller.form.user.UserRegForm;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${jwt.token.name.accessCookieName}")
    private String accessTokenCookieName;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reg")
    private ResponseEntity regUser(@Valid @RequestBody UserRegForm userRegForm) {
        Optional<User> userOptional = userService.addNewUser(userRegForm);
        return userOptional.map(user -> ResponseEntity.status(HttpStatus.CREATED).build())
            .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PostMapping("/account/refreshJWT")
    private ResponseEntity<RefreshTokensForm> refreshJwt(
        @RequestBody RefreshTokensForm refreshTokenForm,
        @CookieValue(name = "${jwt.token.access}") String accessToken,
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
