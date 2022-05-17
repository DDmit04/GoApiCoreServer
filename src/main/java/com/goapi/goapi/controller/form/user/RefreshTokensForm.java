package com.goapi.goapi.controller.form.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.AbstractMap;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class RefreshTokensForm {

    @NotBlank(message = "refresh token can't be empty!")
    private String refreshToken;

    public RefreshTokensForm(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
