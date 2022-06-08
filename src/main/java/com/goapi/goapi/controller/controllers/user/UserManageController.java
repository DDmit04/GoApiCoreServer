package com.goapi.goapi.controller.controllers.user;

import com.goapi.goapi.controller.forms.user.email.ChangeUserEmailForm;
import com.goapi.goapi.controller.forms.user.password.ChangeUserPasswordForm;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facase.user.UserEmailServiceFacade;
import com.goapi.goapi.service.interfaces.facase.user.UserPasswordServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserManageController {

    private final UserEmailServiceFacade userEmailServiceFacade;
    private final UserPasswordServiceFacade userPasswordServiceFacade;

    @GetMapping("/email/change")
    public ResponseEntity requestChangeEmail(@AuthenticationPrincipal User user) {
        boolean sent = userEmailServiceFacade.requestChangeUserEmail(user);
        if (sent) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/email/change")
    public ResponseEntity changeEmail(@AuthenticationPrincipal User user, @Valid @RequestBody ChangeUserEmailForm changeUserEmailForm) {
        boolean changed = userEmailServiceFacade.tryChangeUserEmail(user, changeUserEmailForm);
        if (changed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/email/confirm")
    public ResponseEntity resendConfirmEmail(@AuthenticationPrincipal User user) {
        boolean sent = userEmailServiceFacade.requestConfirmEmail(user);
        if (sent) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity changePassword(@AuthenticationPrincipal User user, @Valid @RequestBody ChangeUserPasswordForm changeUserPasswordForm) {
        boolean changed = userPasswordServiceFacade.changePassword(user, changeUserPasswordForm);
        if (changed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
