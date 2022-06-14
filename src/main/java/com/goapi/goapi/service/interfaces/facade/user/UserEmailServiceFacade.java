package com.goapi.goapi.service.interfaces.facade.user;

import com.goapi.goapi.controller.forms.user.email.UserEmailChangeForm;
import com.goapi.goapi.domain.model.user.User;

public interface UserEmailServiceFacade {

    void tryConfirmEmail(String emailConfirmCode);

    void requestConfirmEmail(User user);

    void tryChangeUserEmail(User user, UserEmailChangeForm userEmailChangeForm);

}
