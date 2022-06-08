package com.goapi.goapi.controller.forms.user.password;

import com.goapi.goapi.controller.forms.SecurityTokenForm;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class ResetUserPasswordForm extends SecurityTokenForm {

    private final String newPassword;

    public ResetUserPasswordForm(String token, String newPassword) {
        super(token);
        this.newPassword = newPassword;
    }
}
