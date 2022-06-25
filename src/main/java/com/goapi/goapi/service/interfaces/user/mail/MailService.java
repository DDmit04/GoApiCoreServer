package com.goapi.goapi.service.interfaces.user.mail;

import com.goapi.goapi.domain.model.user.User;

/**
 * @author Daniil Dmitrochenkov
 **/
public interface MailService {

    boolean sendPasswordRecoverCode(User user, String code);
    boolean sendPasswordSuccessfullyChanged(User user);
    boolean sendEmailConfirmCode(User user, String code);
    boolean sendEmailConfirmed(User user);
    boolean sendEmailChangeCode(User user, String code);
}
