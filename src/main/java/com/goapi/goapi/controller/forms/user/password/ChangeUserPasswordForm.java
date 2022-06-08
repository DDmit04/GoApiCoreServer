package com.goapi.goapi.controller.forms.user.password;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class ChangeUserPasswordForm {

    private final String oldPassword;
    private final String newPassword;

    public ChangeUserPasswordForm(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
