package com.goapi.goapi.controller.forms.user;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class PasswordForm {

    @NotBlank(message = "password can't be blank!")
    private String password;

    public PasswordForm(String password) {
        this.password = password;
    }
}
