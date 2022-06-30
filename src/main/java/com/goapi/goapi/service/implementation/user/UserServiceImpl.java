package com.goapi.goapi.service.implementation.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.UserRoles;
import com.goapi.goapi.domain.model.user.token.EmailSecurityToken;
import com.goapi.goapi.exception.user.UserAlreadyExistsException;
import com.goapi.goapi.repo.UserRepo;
import com.goapi.goapi.service.interfaces.user.UserService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public User addNewUser(UserRegForm userRegForm, UserBill userBill) {
        String username = userRegForm.getUsername();
        String email = userRegForm.getEmail();
        String password = userRegForm.getPassword();
        String jwtToken = UUID.randomUUID().toString();
        Optional<User> userOptional = userRepo.findUserByUsernameOrEmail(username, email);
        if (!userOptional.isPresent()) {
            String encodedPassword = passwordEncoder.encode(password);
            Set<UserRoles> userRoles = Collections.singleton(UserRoles.USER);
            User newUser = new User(username, encodedPassword, email, userRoles, userBill, jwtToken);
            return userRepo.save(newUser);
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
    public void confirmEmail(EmailSecurityToken emailSecurityToken) {
        User user = emailSecurityToken.getUser();
        String confirmingEmail = emailSecurityToken.getConfirmingEmail();
        user.setEmail(confirmingEmail);
        user.setEmailConfirmed(true);
        userRepo.save(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setUserPassword(encodedPassword);
        userRepo.save(user);
    }

    @Override
    public boolean checkEmailExists(String newEmail) {
        Optional<User> userByEmail = userRepo.findUserByEmail(newEmail);
        return userByEmail.isPresent();
    }

}
