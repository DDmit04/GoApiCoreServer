package com.goapi.goapi.service.implementation.user.mail;

import com.goapi.goapi.UrlUtils;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.props.mailProps.EmailLocalizationSubjectNamesProperties;
import com.goapi.goapi.props.mailProps.EmailTemplateNamesProperties;
import com.goapi.goapi.service.interfaces.user.mail.MailService;
import com.goapi.goapi.service.interfaces.user.mail.MyMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final ResourceBundleMessageSource messageSource;

    private final MyMailSender myMailSender;

    private final EmailTemplateNamesProperties emailTemplateNamesProperties;
    private final EmailLocalizationSubjectNamesProperties emailLocalizationSubjectNamesProperties;

    @Value("${urls.email-confirm.path.param-name.confirm-code}")
    private String emailConfirmCodePathVarName;
    @Value("${urls.password-reset.path.param-name.confirm-code}")
    private String passwordResetCodePathVarName;
    @Value("${urls.email-confirm.path.start}")
    private String emailConfirmUrlTemplate;
    @Value("${urls.password-reset.path.start}")
    private String passwordResetUrlTemplate;

    @Value("${email.app-name.value}")
    private String appName;

    @Override
    public boolean sendPasswordRecoverCode(User user, String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String passwordResetLink = getPasswordResetLink(code);
        Map<String, Object> templateModel = new HashMap<>() {{
            put("username", user.getUsername());
            put("recoverLink", passwordResetLink);
        }};
        String passwordRecoverPropName = emailLocalizationSubjectNamesProperties.getPasswordRecover();
        String passwordRecoverSubject = createEmailSubject(passwordRecoverPropName, locale);
        String userEmail = user.getEmail();
        String passwordResetTemplateName = emailTemplateNamesProperties.getPasswordReset();
        boolean mailSent = myMailSender.send(userEmail, passwordRecoverSubject, passwordResetTemplateName, templateModel, locale);
        return mailSent;
    }

    @Override
    public boolean sendPasswordSuccessfullyChanged(User user) {
        Locale locale = LocaleContextHolder.getLocale();
        String username = user.getUsername();
        Map<String, Object> templateModel = new HashMap<>(){{
            put("username", username);
        }};
        String passwordChangedPropName = emailLocalizationSubjectNamesProperties.getPasswordChanged();
        String passwordChangedSubject = createEmailSubject(passwordChangedPropName, locale);
        String userEmail = user.getEmail();
        String passwordChangedTemplateName = emailTemplateNamesProperties.getPasswordChanged();
        boolean mailSent = myMailSender.send(userEmail, passwordChangedSubject, passwordChangedTemplateName, templateModel, locale);
        return mailSent;
    }

    @Override
    public boolean sendEmailConfirmCode(User user, String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String username = user.getUsername();
        String emailConfirmLink = getAccountConfirmLink(code);
        Map<String, Object> templateModel = new HashMap<>(){{
            put("username", username);
            put("emailConfirmLink", emailConfirmLink);
        }};
        String accountCreatedPropName = emailLocalizationSubjectNamesProperties.getAccountCreated();
        String accountCreatedSubject = createEmailSubject(accountCreatedPropName, locale);
        String userEmail = user.getEmail();
        String accountCreatedTemplateName = emailTemplateNamesProperties.getAccountCreated();
        boolean mailSent = myMailSender.send(userEmail, accountCreatedSubject, accountCreatedTemplateName, templateModel, locale);
        return mailSent;
    }

    @Override
    public boolean sendEmailConfirmed(User user) {
        Locale locale = LocaleContextHolder.getLocale();
        String username = user.getUsername();
        Map<String, Object> templateModel = new HashMap<>() {{
            put("username", username);
        }};
        String accountConfirmedPropName = emailLocalizationSubjectNamesProperties.getAccountConfirmed();
        String accountConfirmedSubject = createEmailSubject(accountConfirmedPropName, locale);
        String userEmail = user.getEmail();
        String accountConfirmedTemplateName = emailTemplateNamesProperties.getAccountConfirmed();
        boolean mailSent = myMailSender.send(userEmail, accountConfirmedSubject, accountConfirmedTemplateName, templateModel, locale);
        return mailSent;
    }

    @Override
    public boolean sendEmailChangeCode(User user, String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String username = user.getUsername();
        Map<String, Object> templateModel = new HashMap<>() {{
            put("username", username);
            put("emailChangeLink", code);
        }};
        String accountConfirmedPropName = emailLocalizationSubjectNamesProperties.getEmailChange();
        String accountConfirmedSubject = createEmailSubject(accountConfirmedPropName, locale);
        String userEmail = user.getEmail();
        String accountConfirmedTemplateName = emailTemplateNamesProperties.getEmailChange();
        boolean mailSent = myMailSender.send(userEmail, accountConfirmedSubject, accountConfirmedTemplateName, templateModel, locale);
        return mailSent;
    }

    private String getAccountConfirmLink(String tokenString) {
        Map<String, String> params = new HashMap<>(){{
            put(emailConfirmCodePathVarName, tokenString);
        }};
        return UrlUtils.addQueryParamsToUrl(emailConfirmUrlTemplate, params);
    }

    private String getPasswordResetLink(String tokenString) {
        Map<String, String> params = new HashMap<>(){{
            put(passwordResetCodePathVarName, tokenString);
        }};
        return UrlUtils.addQueryParamsToUrl(passwordResetUrlTemplate, params);
    }

    private String createEmailSubject(String subjectProp, Locale locale) {
        String accountConfirmedSubject = getLocalizedMessage(subjectProp, locale);
        String res = String.format("'%s' %s", appName, accountConfirmedSubject);
        return res;
    }

    private String getLocalizedMessage(String prop, Locale locale) {
        String message = "";
        try {
            message = messageSource.getMessage(prop, null, locale);
        } catch (Exception e) {
            message = prop + ": localization error";
        }
        return message;
    }

}
