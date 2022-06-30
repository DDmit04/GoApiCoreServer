package com.goapi.goapi.controller.forms;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class SecurityTokenForm {

    @NotBlank(message = "security token can't be blank!")
    private String token;

    public SecurityTokenForm(String token) {
        this.token = token;
    }

    public SecurityTokenForm() {
    }
}
