package com.goapi.goapi.controller.forms.user;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class PasswordForm {

    private String password;

    public PasswordForm(String password) {
        this.password = password;
    }
}
