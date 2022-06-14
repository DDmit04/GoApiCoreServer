package com.goapi.goapi.service.interfaces.user.securityToken;

import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.EmailSecurityToken;

public interface EmailSecurityTokenService {
    EmailSecurityToken createNewEmailConfirmToken(User user);

    EmailSecurityToken createNewEmailChangeToken(User user);

    EmailSecurityToken findEmailToken(String tokenString);

    void invalidateEmailToken(EmailSecurityToken token);
}
