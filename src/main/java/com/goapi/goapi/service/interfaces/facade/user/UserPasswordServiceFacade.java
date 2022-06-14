package com.goapi.goapi.service.interfaces.facade.user;

import com.goapi.goapi.controller.forms.user.UsernameOrEmailForm;
import com.goapi.goapi.controller.forms.user.password.ChangeUserPasswordForm;
import com.goapi.goapi.controller.forms.user.password.ResetUserPasswordForm;
import com.goapi.goapi.domain.model.user.User;

public interface UserPasswordServiceFacade {

    void requestResetPassword(UsernameOrEmailForm usernameOrEmailForm);

    void resetPassword(ResetUserPasswordForm resetUserPasswordForm, String resetPasswordCode);

    void changePassword(User user, ChangeUserPasswordForm changeUserPasswordForm);

}
