package com.goapi.goapi.controller.controllers.user;

import com.goapi.goapi.controller.forms.user.email.UserEmailChangeForm;
import com.goapi.goapi.controller.forms.user.password.ChangeUserPasswordForm;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facade.user.UserEmailServiceFacade;
import com.goapi.goapi.service.interfaces.facade.user.UserPasswordServiceFacade;
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

    @PostMapping("/email/change")
    public ResponseEntity changeEmail(@AuthenticationPrincipal User user, @Valid @RequestBody UserEmailChangeForm userEmailChangeForm) {
        userEmailServiceFacade.tryChangeUserEmail(user, userEmailChangeForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/confirm")
    public ResponseEntity resendConfirmEmail(@AuthenticationPrincipal User user) {
        userEmailServiceFacade.requestConfirmEmail(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity changePassword(@AuthenticationPrincipal User user, @Valid @RequestBody ChangeUserPasswordForm changeUserPasswordForm) {
        userPasswordServiceFacade.changePassword(user, changeUserPasswordForm);
        return ResponseEntity.ok().build();
    }

}
