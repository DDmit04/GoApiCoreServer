package com.goapi.goapi.service.interfaces.facade.user;

import com.goapi.goapi.controller.forms.user.UserRegForm;
import com.goapi.goapi.domain.dto.UserAuthDto;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserServiceFacade {

    User createNewUser(UserRegForm userRegForm);
    UserAuthDto refreshJwtTokens(String refreshToken);
    User addRefreshJwtToken(User user);
}
