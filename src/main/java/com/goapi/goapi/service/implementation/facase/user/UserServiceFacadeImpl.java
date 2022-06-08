package com.goapi.goapi.service.implementation.facase.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.controller.forms.user.auth.UserAuthInfo;
import com.goapi.goapi.domain.model.token.SecurityToken;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.service.interfaces.SecurityTokenService;
import com.goapi.goapi.service.interfaces.facase.user.UserServiceFacade;
import com.goapi.goapi.service.interfaces.mail.MailService;
import com.goapi.goapi.service.interfaces.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserServiceFacadeImpl implements UserServiceFacade {

    private final MailService mailService;
    private final UserService userService;
    private final JwtUtils jwtTokenUtil;

    private final SecurityTokenService securityTokenService;

    @Override
    public User createNewUser(UserRegForm userRegForm) {
        User user = userService.addNewUser(userRegForm);
        SecurityToken newEmailConfirmToken = securityTokenService.createNewEmailConfirmToken(user);
        String tokenString = newEmailConfirmToken.getToken();
        mailService.sendEmailConfirmCode(user, tokenString);
        return user;
    }

    @Override
    public UserAuthInfo refreshJwtTokens(String refreshJwtToken) {
        boolean tokenIsValid = jwtTokenUtil.validateToken(refreshJwtToken);
        if(tokenIsValid) {
            String givenUserRefreshToken = jwtTokenUtil.getTokenSubject(refreshJwtToken);
            User user = userService.findUserByJwtRefreshToken(givenUserRefreshToken);
            String trueUserRefreshToken = user.getJwtRefreshToken();
            boolean tokenMatches = givenUserRefreshToken.equals(trueUserRefreshToken);
            if (tokenMatches) {
                return userService.refreshJwtTokens(user);
            }
        }
        throw new BadCredentialsException("Refresh token is invalid!");
    }

    @Override
    public User addRefreshJwtToken(User user) {
        UUID uuid = UUID.randomUUID();
        String newTokenString = uuid.toString();
        user.setJwtRefreshToken(newTokenString);
        user = userService.addJwtRefreshToken(user, newTokenString);
        return user;
    }

}
