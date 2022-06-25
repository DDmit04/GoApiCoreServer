package com.goapi.goapi.controller.forms.user.password;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class ChangeUserPasswordForm {

    @NotBlank(message = "old password can't be blank!")
    private final String oldPassword;
    @NotBlank(message = "new password can't be blank!")
    private final String newPassword;

    public ChangeUserPasswordForm(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
