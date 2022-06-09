package com.goapi.goapi.service.implementation.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.controller.forms.user.auth.UserAuthInfo;
import com.goapi.goapi.domain.model.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.UserRoles;
import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import com.goapi.goapi.exception.user.UserAlreadyExistsException;
import com.goapi.goapi.repo.UserRepo;
import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.service.interfaces.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
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

    @Override
    public User addNewUser(UserRegForm userRegForm, UserBill userAppServiceBill) {
        String username = userRegForm.getUsername();
        String email = userRegForm.getEmail();
        String password = userRegForm.getPassword();
        Optional<User> userOptional = userRepo.findUserByUsernameOrEmail(username, email);
        if (!userOptional.isPresent()) {
            String encodedPassword = passwordEncoder.encode(password);
            Set<UserRoles> userRoles = Collections.singleton(UserRoles.USER);
            User newUser = new User(username, encodedPassword, email, userRoles, userAppServiceBill);
            UUID jwtRefreshTokenUuid = UUID.randomUUID();
            newUser = addJwtRefreshToken(newUser, jwtRefreshTokenUuid.toString());
            return newUser;
        }
        throw new UserAlreadyExistsException(username, email);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findUserByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        String userNotFoundMessage = String.format("User with username or email '%s' not found!", usernameOrEmail);
        return userOptional
            .orElseThrow(() -> new UsernameNotFoundException(userNotFoundMessage));
    }

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
    public void invalidateJwtRefreshToken(String username) {
        User user = (User) loadUserByUsername(username);
        invalidateJwtRefreshToken(user);
    }

    @Override
    public void invalidateJwtRefreshToken(User user) {
        UUID newUuid = UUID.randomUUID();
        user.setJwtRefreshToken(newUuid.toString());
        userRepo.save(user);
    }

    @Override
    public void saveUserApiTariff(User user, UserApiTariff newTariff) {
        user.setUserApiTariff(newTariff);
        userRepo.save(user);
    }

    @Override
    public void confirmEmail(User user) {
        user.setEmailConfirmed(true);
        userRepo.save(user);
    }

    @Override
    public User updateEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        user.setEmailConfirmed(false);
        user = userRepo.save(user);
        return user;
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setUserPassword(encodedPassword);
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

    @Override
    public boolean checkEmailExists(String newEmail) {
        Optional<User> userByEmail = userRepo.findUserByEmail(newEmail);
        return userByEmail.isPresent();
    }

}
