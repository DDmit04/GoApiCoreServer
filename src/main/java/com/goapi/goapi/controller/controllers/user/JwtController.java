package com.goapi.goapi.controller.controllers.user;

import com.goapi.goapi.controller.forms.user.auth.UserAuthInfo;
import com.goapi.goapi.service.interfaces.facade.user.UserServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping
@RequiredArgsConstructor
public class JwtController {

    @Value("${tokens.name.refresh}")
    private String refreshTokenCookieName;

    @Value("${tokens.name.access}")
    private String accessTokenFieldName;

    @Value("${urls.jwr-refresh.path.final}")
    private String refreshJwtUrl;
    private final UserServiceFacade userServiceFacade;

    @PostMapping("${urls.jwr-refresh.path.final}")
    private ResponseEntity<Map<String, String>> refreshJwt(@CookieValue(name = "${tokens.name.refresh}") String refreshToken, HttpServletResponse response) {

        UserAuthInfo userInfo = userServiceFacade.refreshJwtTokens(refreshToken);
        String newRefreshToken = userInfo.getRefreshToken();
        String newAccessToken = userInfo.getAccessToken();

        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath(refreshJwtUrl);
        response.addCookie(refreshTokenCookie);
        Map<String, String> body = new HashMap<>() {{
            put(accessTokenFieldName, newAccessToken);
        }};
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(body);
    }

}
