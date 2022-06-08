package com.goapi.goapi.controller.forms;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class SecurityTokenForm {

    private final String token;

    public SecurityTokenForm(String token) {
        this.token = token;
    }
}
