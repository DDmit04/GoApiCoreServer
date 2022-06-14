package com.goapi.goapi.service.interfaces.user;

import com.goapi.goapi.controller.forms.user.auth.UserAuthInfo;
import com.goapi.goapi.domain.model.user.User;

public interface UserJwtService {
    UserAuthInfo refreshJwtTokens(User user);

    void invalidateJwtRefreshToken(User user);

    User findUserByJwtRefreshToken(String refreshToken);

    User addJwtRefreshToken(User user, String newTokenString);
}
