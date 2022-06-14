package com.goapi.goapi.controller.forms.user.email;

import lombok.Getter;

import javax.validation.constraints.Email;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserEmailChangeForm {

    private final String password;
    @Email
    private final String email;

    public UserEmailChangeForm(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
