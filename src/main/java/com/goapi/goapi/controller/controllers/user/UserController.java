package com.goapi.goapi.controller.controllers.user;

import com.goapi.goapi.controller.forms.SecurityTokenForm;
import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.controller.forms.user.UsernameOrEmailForm;
import com.goapi.goapi.controller.forms.user.password.ResetUserPasswordForm;
import com.goapi.goapi.service.interfaces.facade.user.UserEmailServiceFacade;
import com.goapi.goapi.service.interfaces.facade.user.UserPasswordServiceFacade;
import com.goapi.goapi.service.interfaces.facade.user.UserServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/anon")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceFacade userServiceFacade;
    private final UserEmailServiceFacade userEmailServiceFacade;
    private final UserPasswordServiceFacade userPasswordServiceFacade;

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody UserRegForm userRegForm) {
        userServiceFacade.createNewUser(userRegForm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/email/confirm")
    public ResponseEntity confirmEmail(@Valid @RequestBody SecurityTokenForm securityTokenForm) {
        String emailConfirmCode = securityTokenForm.getToken();
        userEmailServiceFacade.tryConfirmEmail(emailConfirmCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity requestResetPassword(@Valid @RequestBody UsernameOrEmailForm usernameOrEmailForm) {
        userPasswordServiceFacade.requestResetPassword(usernameOrEmailForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetUserPasswordForm resetUserPasswordForm) {
        String resetPasswordCode = resetUserPasswordForm.getToken();
        userPasswordServiceFacade.resetPassword(resetUserPasswordForm, resetPasswordCode);
        return ResponseEntity.ok().build();
    }

}
