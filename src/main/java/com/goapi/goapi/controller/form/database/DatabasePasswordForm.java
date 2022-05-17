package com.goapi.goapi.controller.form.database;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabasePasswordForm {

    @NotBlank(message = "db password can't be blank!")
    private String password;

    public DatabasePasswordForm(String password) {
        this.password = password;
    }
}
