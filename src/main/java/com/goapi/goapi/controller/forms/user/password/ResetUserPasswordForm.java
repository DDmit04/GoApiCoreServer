package com.goapi.goapi.controller.forms.user.password;

import com.goapi.goapi.controller.forms.SecurityTokenForm;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class ResetUserPasswordForm extends SecurityTokenForm {

    @NotBlank(message = "new password can't be blank!")
    private String newPassword;

    public ResetUserPasswordForm(String token, String newPassword) {
        super(token);
        this.newPassword = newPassword;
    }

    public ResetUserPasswordForm() {
    }
}
