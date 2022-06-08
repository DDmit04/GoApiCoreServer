package com.goapi.goapi.service.implementation.facase.user;

import com.goapi.goapi.controller.forms.user.UsernameOrEmailForm;
import com.goapi.goapi.controller.forms.user.password.ChangeUserPasswordForm;
import com.goapi.goapi.controller.forms.user.password.ResetUserPasswordForm;
import com.goapi.goapi.domain.model.token.SecurityToken;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.user.PasswordNotMatchingException;
import com.goapi.goapi.exception.user.UserEmailNotConfirmedException;
import com.goapi.goapi.service.interfaces.SecurityTokenService;
import com.goapi.goapi.service.interfaces.facase.user.UserPasswordServiceFacade;
import com.goapi.goapi.service.interfaces.mail.MailService;
import com.goapi.goapi.service.interfaces.user.UserService;
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
    private final PasswordEncoder passwordEncoder;
    private final SecurityTokenService securityTokenService;
    private final MailService mailService;

    @Override
    public void requestResetPassword(UsernameOrEmailForm usernameOrEmailForm) {
        String usernameOrEmail = usernameOrEmailForm.getUsernameOrEmail();
        User user = (User) userService.loadUserByUsername(usernameOrEmail);
        boolean isEmailConfirmed = user.isEmailConfirmed();
        if (isEmailConfirmed) {
            SecurityToken newPasswordToken = securityTokenService.createNewPasswordResetToken(user);
            String tokenString = newPasswordToken.getToken();
            mailService.sendPasswordRecoverCode(user, tokenString);
            return;
        }
        Integer userId = user.getId();
        throw new UserEmailNotConfirmedException(userId);
    }

    @Override
    public boolean resetPassword(ResetUserPasswordForm resetUserPasswordForm, String resetPasswordCode) {
        SecurityToken passwordResetToken = securityTokenService.findPasswordResetToken(resetPasswordCode);
        User user = passwordResetToken.getUser();
        String newPassword = resetUserPasswordForm.getNewPassword();
        userService.updatePassword(user, newPassword);
        securityTokenService.invalidateToken(passwordResetToken);
        mailService.sendPasswordSuccessfullyChanged(user);
        return true;
    }

    @Override
    public boolean changePassword(User user, ChangeUserPasswordForm changeUserPasswordForm) {
        String givenOldPassword = changeUserPasswordForm.getOldPassword();
        String trueEncodedOldPassword = user.getUserPassword();
        boolean passwordMatch = passwordEncoder.matches(givenOldPassword, trueEncodedOldPassword);
        if (passwordMatch) {
            String newPassword = changeUserPasswordForm.getNewPassword();
            userService.updatePassword(user, newPassword);
            userService.invalidateJwtRefreshToken(user);
            mailService.sendPasswordSuccessfullyChanged(user);
        }
        Integer userId = user.getId();
        throw new PasswordNotMatchingException(userId);
    }

}
