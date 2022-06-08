package com.goapi.goapi.controller.controllers.user;

import com.goapi.goapi.controller.forms.SecurityTokenForm;
import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.controller.forms.user.UsernameOrEmailForm;
import com.goapi.goapi.controller.forms.user.password.ResetUserPasswordForm;
import com.goapi.goapi.service.interfaces.facase.user.UserEmailServiceFacade;
import com.goapi.goapi.service.interfaces.facase.user.UserPasswordServiceFacade;
import com.goapi.goapi.service.interfaces.facase.user.UserServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/email/confirm")
    public ResponseEntity confirmEmail(@Valid @RequestBody SecurityTokenForm securityTokenForm) {
        String emailConfirmCode = securityTokenForm.getToken();
        boolean confirmed = userEmailServiceFacade.tryConfirmEmail(emailConfirmCode);
        if (confirmed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/password/reset")
    public ResponseEntity requestResetPassword(@Valid @RequestBody UsernameOrEmailForm usernameOrEmailForm) {
        userPasswordServiceFacade.requestResetPassword(usernameOrEmailForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetUserPasswordForm resetUserPasswordForm) {
        String resetPasswordCode = resetUserPasswordForm.getToken();
        boolean reset = userPasswordServiceFacade.resetPassword(resetUserPasswordForm, resetPasswordCode);
        if (reset) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
