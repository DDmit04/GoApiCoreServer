package com.goapi.goapi.controller.form.user;

import com.goapi.goapi.domain.user.User;
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
