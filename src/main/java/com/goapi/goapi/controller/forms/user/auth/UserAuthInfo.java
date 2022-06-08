package com.goapi.goapi.controller.forms.user.auth;

import com.goapi.goapi.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@AllArgsConstructor
@Getter
public class UserAuthInfo {

    private User user;
    private String refreshToken;
    private String accessToken;

}
