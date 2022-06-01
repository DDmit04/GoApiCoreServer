package com.goapi.goapi.controller.form.database;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class GetDatabasePasswordForm {

    private String password;

    public GetDatabasePasswordForm(String password) {
        this.password = password;
    }
}
