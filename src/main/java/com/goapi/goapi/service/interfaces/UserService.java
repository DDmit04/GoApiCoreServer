package com.goapi.goapi.service.interfaces;

import com.goapi.goapi.controller.form.user.UserAuthInfo;
import com.goapi.goapi.controller.form.user.UserRegForm;
import com.goapi.goapi.domain.dto.tariff.UserApiTariffDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserAuthInfo refreshTokens(String accessToken, String refreshToken);

    Optional<User> addNewUser(UserRegForm userRegForm);

    @Override
    UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException;

    void invalidateAccessToken(String username);

    boolean changeUserApiTariff(User user, UserApiTariff newTariff);

    UserApiTariffDto getUserApiTariff(User user);
}
