package com.goapi.goapi.service.implementation.facade.user;

import com.goapi.goapi.controller.forms.user.email.UserEmailChangeForm;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.EmailSecurityToken;
import com.goapi.goapi.exception.user.PasswordNotMatchingException;
import com.goapi.goapi.service.interfaces.facade.user.UserEmailServiceFacade;
import com.goapi.goapi.service.interfaces.user.UserService;
import com.goapi.goapi.service.interfaces.user.mail.MailService;
import com.goapi.goapi.service.interfaces.user.securityToken.EmailSecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserEmailServiceFacadeFacadeImpl implements UserEmailServiceFacade {

    private final UserService userService;
    private final EmailSecurityTokenService emailSecurityTokenService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void tryConfirmEmail(String tokenString) {
        EmailSecurityToken token = emailSecurityTokenService.findEmailToken(tokenString);
        User user = token.getUser();
        userService.confirmEmail(token);
        emailSecurityTokenService.invalidateEmailToken(token);
        mailService.sendEmailConfirmed(user);
    }

    @Override
    public void requestConfirmEmail(User user) {
        EmailSecurityToken newEmailConfirmToken = emailSecurityTokenService.createNewEmailConfirmToken(user);
        String tokenString = newEmailConfirmToken.getToken();
        mailService.sendEmailConfirmCode(user, tokenString);
    }

    @Override
    public void tryChangeUserEmail(User user, UserEmailChangeForm userEmailChangeForm) {
        String encodedUserPassword = user.getUserPassword();
        String givenPassword = userEmailChangeForm.getPassword();
        boolean passwordMatches = passwordEncoder.matches(givenPassword, encodedUserPassword);
        if(passwordMatches) {
            String newEmail = userEmailChangeForm.getEmail();
            String userEmail = user.getEmail();
            boolean emailExists = userService.checkEmailExists(newEmail);
            boolean emailIsNew = userEmail.equals(newEmail);
            boolean canChangeEmail = emailIsNew && !emailExists;
            if(canChangeEmail) {
                EmailSecurityToken newEmailConfirmToken = emailSecurityTokenService.createNewEmailChangeToken(user);
                String tokenString = newEmailConfirmToken.getToken();
                mailService.sendEmailChangeCode(user, tokenString);
            }
            Integer userId = user.getId();
            throw new PasswordNotMatchingException(userId);
        }
        //do not return email errors!
    }
}
