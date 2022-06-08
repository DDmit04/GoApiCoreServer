package com.goapi.goapi.service.interfaces.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.controller.forms.user.auth.UserAuthInfo;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    User addNewUser(UserRegForm userRegForm);

    @Override
    UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException;

    UserAuthInfo refreshJwtTokens(User user);

    void invalidateJwtRefreshToken(String username);

    void invalidateJwtRefreshToken(User user);

    void saveUserApiTariff(User user, UserApiTariff newTariff);

    void confirmEmail(User user);

    User updateEmail(User user, String newEmail);

    void updatePassword(User user, String newPassword);

    User findUserByJwtRefreshToken(String refreshToken);

    User addJwtRefreshToken(User user, String newTokenString);

    boolean checkEmailExists(String newEmail);
}
