package com.goapi.goapi.service.implementation.facade.user;

import com.goapi.goapi.controller.forms.user.UsernameOrEmailForm;
import com.goapi.goapi.controller.forms.user.password.ChangeUserPasswordForm;
import com.goapi.goapi.controller.forms.user.password.ResetUserPasswordForm;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.PasswordSecurityToken;
import com.goapi.goapi.domain.model.user.token.SecurityToken;
import com.goapi.goapi.exception.user.PasswordNotMatchingException;
import com.goapi.goapi.exception.user.PasswordsAreEqualException;
import com.goapi.goapi.exception.user.UserEmailNotConfirmedException;
import com.goapi.goapi.service.interfaces.facade.user.UserPasswordServiceFacade;
import com.goapi.goapi.service.interfaces.user.UserJwtService;
import com.goapi.goapi.service.interfaces.user.UserService;
import com.goapi.goapi.service.interfaces.user.mail.MailService;
import com.goapi.goapi.service.interfaces.user.securityToken.PasswordSecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserPasswordServiceFacadeImpl implements UserPasswordServiceFacade {

    private final UserService userService;
    private final UserJwtService userJwtService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordSecurityTokenService passwordSecurityTokenService;
    private final MailService mailService;

    @Override
    public void requestResetPassword(UsernameOrEmailForm usernameOrEmailForm) {
        String usernameOrEmail = usernameOrEmailForm.getUsernameOrEmail();
        User user = (User) userService.loadUserByUsername(usernameOrEmail);
        boolean isEmailConfirmed = user.isEmailConfirmed();
        if (isEmailConfirmed) {
            SecurityToken newPasswordToken = passwordSecurityTokenService.createNewPasswordResetToken(user);
            String tokenString = newPasswordToken.getToken();
            mailService.sendPasswordRecoverCode(user, tokenString);
            return;
        }
        Integer userId = user.getId();
        throw new UserEmailNotConfirmedException(userId);
    }

    @Override
    public void resetPassword(ResetUserPasswordForm resetUserPasswordForm, String resetPasswordCode) {
        PasswordSecurityToken passwordResetToken = passwordSecurityTokenService.findPasswordResetToken(resetPasswordCode);
        User user = passwordResetToken.getUser();
        String encodedUserPassword = user.getPassword();
        String newPassword = resetUserPasswordForm.getNewPassword();
        boolean passwordMatch = passwordEncoder.matches(newPassword, encodedUserPassword);
        if(!passwordMatch) {
            userService.updatePassword(user, newPassword);
            passwordSecurityTokenService.invalidatePasswordResetToken(passwordResetToken);
            mailService.sendPasswordSuccessfullyChanged(user);
        } else {
            Integer userId = user.getId();
            throw new PasswordsAreEqualException(userId);
        }
    }

    @Override
    public void changePassword(User user, ChangeUserPasswordForm changeUserPasswordForm) {
        Integer userId = user.getId();
        String givenOldPassword = changeUserPasswordForm.getOldPassword();
        String givenNewPassword = changeUserPasswordForm.getNewPassword();
        String encodedUserPassword = user.getUserPassword();
        boolean oldPasswordMatch = passwordEncoder.matches(givenOldPassword, encodedUserPassword);
        boolean newPasswordMatch = passwordEncoder.matches(givenNewPassword, encodedUserPassword);
        boolean canChangePassword = oldPasswordMatch && !newPasswordMatch;
        if (canChangePassword) {
            String newPassword = changeUserPasswordForm.getNewPassword();
            userService.updatePassword(user, newPassword);
            userJwtService.invalidateJwtRefreshToken(user);
            mailService.sendPasswordSuccessfullyChanged(user);
        } else if(newPasswordMatch) {
            throw new PasswordsAreEqualException(userId);
        } else {
            throw new PasswordNotMatchingException(userId);
        }
    }

}
