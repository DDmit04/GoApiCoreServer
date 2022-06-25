package com.goapi.goapi.domain.dto;

import com.goapi.goapi.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@AllArgsConstructor
@Getter
public class UserAuthDto {

    private User user;
    private String refreshToken;
    private String accessToken;

}
