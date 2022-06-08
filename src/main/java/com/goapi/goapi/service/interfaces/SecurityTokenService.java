package com.goapi.goapi.service.interfaces;

import com.goapi.goapi.domain.model.token.SecurityToken;
import com.goapi.goapi.domain.model.user.User;

public interface SecurityTokenService {
    SecurityToken createNewPasswordResetToken(User user);

    SecurityToken createNewEmailConfirmToken(User user);
    SecurityToken createNewEmailChangeCode(User user);
    SecurityToken findEmailConfirmToken(String tokenString);

    void invalidateToken(SecurityToken token);

    SecurityToken findPasswordResetToken(String tokenString);

    SecurityToken findEmailChangeToken(String tokenString);

}
