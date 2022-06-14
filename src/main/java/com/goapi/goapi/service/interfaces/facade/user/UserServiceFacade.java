package com.goapi.goapi.service.interfaces.facade.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.controller.forms.user.auth.UserAuthInfo;
import com.goapi.goapi.domain.model.user.User;

public interface UserServiceFacade {
    User createNewUser(UserRegForm userRegForm);

    UserAuthInfo refreshJwtTokens(String refreshToken);

    User addRefreshJwtToken(User user);
}
