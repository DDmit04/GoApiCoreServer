package com.goapi.goapi.controller.forms.user.email;

import com.goapi.goapi.controller.forms.SecurityTokenForm;
import lombok.Getter;

import javax.validation.constraints.Email;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class ChangeUserEmailForm extends SecurityTokenForm {

    @Email
    private final String newEmail;

    public ChangeUserEmailForm(String code, String newEmail) {
        super(code);
        this.newEmail = newEmail;
    }
}
