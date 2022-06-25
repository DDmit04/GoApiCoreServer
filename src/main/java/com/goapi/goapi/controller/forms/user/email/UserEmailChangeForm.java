package com.goapi.goapi.controller.forms.user.email;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserEmailChangeForm {

    @NotBlank(message = "password can't be blank!")
    private final String password;
    @Email(message = "email must be email!")
    private final String email;

    public UserEmailChangeForm(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
