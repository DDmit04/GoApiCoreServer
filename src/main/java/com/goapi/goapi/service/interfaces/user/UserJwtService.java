package com.goapi.goapi.service.interfaces.user;

import com.goapi.goapi.domain.dto.UserAuthDto;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserJwtService {

    UserAuthDto refreshJwtTokens(User user);
    void invalidateJwtRefreshToken(User user);
    User findUserByJwtRefreshToken(String refreshToken);
    User addJwtRefreshToken(User user, String newTokenString);
}
