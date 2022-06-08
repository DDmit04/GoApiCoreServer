package com.goapi.goapi.service.interfaces.facase.user;

import com.goapi.goapi.controller.forms.user.email.ChangeUserEmailForm;
import com.goapi.goapi.domain.model.user.User;

public interface UserEmailServiceFacade {

    boolean tryConfirmEmail(String emailConfirmCode);

    boolean requestConfirmEmail(User user);

    boolean tryChangeUserEmail(User user, ChangeUserEmailForm changeUserEmailForm);

    boolean requestChangeUserEmail(User user);

}
