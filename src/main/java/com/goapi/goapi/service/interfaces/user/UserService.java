package com.goapi.goapi.service.interfaces.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.EmailSecurityToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService extends UserDetailsService {

    User addNewUser(UserRegForm userRegForm, UserBill userAppServiceBill);
    @Override
    UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException;
    void confirmEmail(EmailSecurityToken emailSecurityToken);
    void updatePassword(User user, String newPassword);
    boolean checkEmailExists(String newEmail);

}
