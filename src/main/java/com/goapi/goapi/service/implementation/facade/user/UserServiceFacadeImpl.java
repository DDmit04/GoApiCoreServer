package com.goapi.goapi.service.implementation.facade.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.domain.dto.UserAuthDto;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.SecurityToken;
import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.service.interfaces.facade.user.UserServiceFacade;
import com.goapi.goapi.service.interfaces.finances.bill.UserBillService;
import com.goapi.goapi.service.interfaces.user.UserJwtService;
import com.goapi.goapi.service.interfaces.user.UserService;
import com.goapi.goapi.service.interfaces.user.mail.MailService;
import com.goapi.goapi.service.interfaces.user.securityToken.EmailSecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserJwtService userJwtService;
    private final JwtUtils jwtTokenUtil;
    private final EmailSecurityTokenService emailSecurityTokenService;
    private final UserBillService userBillService;
    @Value("${string.invalid-refresh-token}")
    private String invalidRefreshTokenMessage;

    @Override
    public User createNewUser(UserRegForm userRegForm) {
        UserBill userBill = userBillService.createUserBill();
        User user = userService.addNewUser(userRegForm, userBill);
        SecurityToken newEmailConfirmToken = emailSecurityTokenService.createNewEmailConfirmToken(user);
        String tokenString = newEmailConfirmToken.getToken();
        mailService.sendEmailConfirmCode(user, tokenString);
        return user;
    }

    @Override
    public UserAuthDto refreshJwtTokens(String refreshJwtToken) {
        boolean tokenIsValid = jwtTokenUtil.validateToken(refreshJwtToken);
        if(tokenIsValid) {
            String givenUserRefreshToken = jwtTokenUtil.getTokenSubject(refreshJwtToken);
            User user = userJwtService.findUserByJwtRefreshToken(givenUserRefreshToken);
            String trueUserRefreshToken = user.getJwtRefreshToken();
            boolean tokenMatches = givenUserRefreshToken.equals(trueUserRefreshToken);
            if (tokenMatches) {
                return userJwtService.refreshJwtTokens(user);
            }
        }
        throw new BadCredentialsException(invalidRefreshTokenMessage);
    }

    @Override
    public User addRefreshJwtToken(User user) {
        UUID uuid = UUID.randomUUID();
        String newTokenString = uuid.toString();
        user.setJwtRefreshToken(newTokenString);
        user = userJwtService.addJwtRefreshToken(user, newTokenString);
        return user;
    }

}
