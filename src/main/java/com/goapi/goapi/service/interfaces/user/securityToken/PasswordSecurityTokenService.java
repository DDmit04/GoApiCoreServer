package com.goapi.goapi.service.interfaces.user.securityToken;

import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.PasswordSecurityToken;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PasswordSecurityTokenService {

    PasswordSecurityToken createNewPasswordResetToken(User user);
    PasswordSecurityToken findPasswordResetToken(String tokenString);
    void invalidatePasswordResetToken(PasswordSecurityToken token);
}
