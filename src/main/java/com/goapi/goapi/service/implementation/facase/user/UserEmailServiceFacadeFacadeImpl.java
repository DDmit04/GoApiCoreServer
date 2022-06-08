package com.goapi.goapi.service.implementation.facase.user;

import com.goapi.goapi.controller.forms.user.email.ChangeUserEmailForm;
import com.goapi.goapi.domain.model.token.SecurityToken;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.user.UserEmailNotConfirmedException;
import com.goapi.goapi.service.interfaces.SecurityTokenService;
import com.goapi.goapi.service.interfaces.facase.user.UserEmailServiceFacade;
import com.goapi.goapi.service.interfaces.mail.MailService;
import com.goapi.goapi.service.interfaces.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserEmailServiceFacadeFacadeImpl implements UserEmailServiceFacade {

    private final UserService userService;
    private final SecurityTokenService securityTokenService;
    private final MailService mailService;

    @Override
    public boolean tryConfirmEmail(String tokenString) {
        SecurityToken token = securityTokenService.findEmailConfirmToken(tokenString);
        User user = token.getUser();
        userService.confirmEmail(user);
        securityTokenService.invalidateToken(token);
        mailService.sendEmailConfirmed(user);
        return true;
    }

    @Override
    public boolean requestConfirmEmail(User user) {
        SecurityToken newEmailConfirmToken = securityTokenService.createNewEmailConfirmToken(user);
        String tokenString = newEmailConfirmToken.getToken();
        mailService.sendEmailConfirmCode(user, tokenString);
        return true;
    }

    @Override
    public boolean requestChangeUserEmail(User user) {
        boolean isEmailConfirmed = user.isEmailConfirmed();
        if(isEmailConfirmed) {
            SecurityToken newEmailChangeToken = securityTokenService.createNewEmailChangeCode(user);
            String tokenString = newEmailChangeToken.getToken();
            mailService.sendEmailChangeCode(user, tokenString);
            return true;
        }
        Integer userId = user.getId();
        throw new UserEmailNotConfirmedException(userId);
    }

    @Override
    public boolean tryChangeUserEmail(User user, ChangeUserEmailForm changeUserEmailForm) {
        String givenEmailConfirmToken = changeUserEmailForm.getToken();
        SecurityToken trueEmailChangeToken = securityTokenService.findEmailChangeToken(givenEmailConfirmToken);
        User tokenUser = trueEmailChangeToken.getUser();
        String newEmail = changeUserEmailForm.getNewEmail();
        String userEmail = user.getEmail();
        boolean emailExists = userService.checkEmailExists(newEmail);
        boolean emailIsNew = userEmail.equals(newEmail);
        boolean usersMatch = user.equals(tokenUser);
        boolean canChangeEmail = usersMatch && emailIsNew && !emailExists;
        if(canChangeEmail) {
            user = userService.updateEmail(user, newEmail);
            requestConfirmEmail(user);
        }
        return true;
    }

}
