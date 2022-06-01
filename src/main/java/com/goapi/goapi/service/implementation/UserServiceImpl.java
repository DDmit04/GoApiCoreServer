package com.goapi.goapi.service.implementation;

import com.goapi.goapi.controller.form.user.UserAuthInfo;
import com.goapi.goapi.controller.form.user.UserRegForm;
import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.UserRoles;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.exception.tariff.userApi.UserApiTariffConditionChangeException;
import com.goapi.goapi.repo.UserRepo;
import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.service.interfaces.UserService;
import com.goapi.goapi.service.interfaces.userApi.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtTokenUtil;
    private final UserApiService userApiService;


    @Override
    public UserAuthInfo refreshTokens(String accessToken, String refreshToken) {
        String username = jwtTokenUtil.getTokenSubject(accessToken);
        String userTokenUuid = jwtTokenUtil.getTokenSubject(refreshToken);
        User user = (User) loadUserByUsername(username);
        if (StringUtils.hasText(refreshToken)
            && jwtTokenUtil.validateToken(refreshToken)
            && userTokenUuid.equals(user.getRefreshTokenUuid().toString())) {
            UUID newUuid = UUID.randomUUID();
            user.setRefreshTokenUuid(newUuid);
            user = userRepo.save(user);
            accessToken = jwtTokenUtil.generateAccessToken(user.getUsername());
            refreshToken = jwtTokenUtil.generateRefreshToken(user.getRefreshTokenUuid().toString());
            return new UserAuthInfo(user, refreshToken, accessToken);
        }
        throw new BadCredentialsException("Refresh token is invalid!");
    }

    @Override
    public Optional<User> addNewUser(UserRegForm userRegForm) {
        String username = userRegForm.getUsername();
        String email = userRegForm.getEmail();
        String password = userRegForm.getPassword();
        Optional<User> userOptional = userRepo.findUserByUsernameOrEmail(username, email);
        if (!userOptional.isPresent()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setRoles(Collections.singleton(UserRoles.USER));
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRefreshTokenUuid(UUID.randomUUID());
            return Optional.of(userRepo.save(newUser));
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findUserByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        return userOptional
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with dbUsername or email '%s' not found!", usernameOrEmail)));
    }

    @Override
    public void invalidateAccessToken(String username) {
        User user = (User) loadUserByUsername(username);
        UUID newUuid = UUID.randomUUID();
        user.setRefreshTokenUuid(newUuid);
        userRepo.save(user);
    }

    @Override
    public boolean changeUserApiTariff(User user, UserApiTariff newTariff) {
        UserApiTariff currentUserApiTariff = user.getUserApiTariff();
        if (currentUserApiTariff == null) {
            saveUserApiTariff(user, newTariff);
            return true;
        } else {
            if (!currentUserApiTariff.equals(newTariff)) {
                int newMaxRequestsCount = newTariff.getMaxRequestsCount();
                Integer userId = user.getId();
                int existingRequestsCount = userApiService.getTotalUserApiRequestsCount(userId);
                boolean canChange = existingRequestsCount <= newMaxRequestsCount;
                if (canChange) {
                    saveUserApiTariff(user, newTariff);
                } else {
                    Integer currentTariffId = currentUserApiTariff.getId();
                    Integer newTariffId = newTariff.getId();
                    throw new UserApiTariffConditionChangeException(newTariffId, currentTariffId);
                }
                return canChange;
            }
            return false;
        }
    }

    @Override
    public UserApiTariffDto getUserApiTariff(User user) {
        UserApiTariff userApiTariff = user.getUserApiTariff();
        UserApiTariffDto userApiTariffDto = new UserApiTariffDto(
            userApiTariff.getId(),
            userApiTariff.getName(),
            userApiTariff.getCostPerMonth(),
            userApiTariff.getMaxRequestsCount());
        return userApiTariffDto;
    }

    private void saveUserApiTariff(User user, UserApiTariff newTariff) {
        user.setUserApiTariff(newTariff);
        userRepo.save(user);
    }
}
