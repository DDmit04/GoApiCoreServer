package com.goapi.goapi.service.implementation.user;

import com.goapi.goapi.controller.forms.user.auth.UserAuthInfo;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.repo.UserRepo;
import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.service.interfaces.user.UserJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@RequiredArgsConstructor
@Service
public class UserJwtServiceImpl implements UserJwtService {

    private final UserRepo userRepo;
    private final JwtUtils jwtTokenUtil;

    @Override
    public UserAuthInfo refreshJwtTokens(User user) {
        UUID newUuid = UUID.randomUUID();
        String newJwtRefreshToken = newUuid.toString();
        user = addJwtRefreshToken(user, newJwtRefreshToken);
        String username = user.getUsername();
        newJwtRefreshToken = user.getJwtRefreshToken();
        String accessToken = jwtTokenUtil.generateAccessToken(username);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(newJwtRefreshToken);
        return new UserAuthInfo(user, newRefreshToken, accessToken);
    }

    @Override
    public void invalidateJwtRefreshToken(User user) {
        UUID newUuid = UUID.randomUUID();
        user.setJwtRefreshToken(newUuid.toString());
        userRepo.save(user);
    }

    @Override
    public User findUserByJwtRefreshToken(String refreshToken) {
        Optional<User> userOptional = userRepo.findByJwtRefreshToken(refreshToken);
        return userOptional.orElseThrow(() -> new BadCredentialsException("Refresh token not found!"));
    }

    @Override
    public User addJwtRefreshToken(User user, String newTokenString) {
        user.setJwtRefreshToken(newTokenString);
        user = userRepo.save(user);
        return user;
    }

}
