package com.goapi.goapi.controller.forms.user.auth;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class AccessTokensForm {

    @NotBlank(message = "refresh token can't be empty!")
    private String accessToken;

    public AccessTokensForm(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessTokensForm() {
    }
}
