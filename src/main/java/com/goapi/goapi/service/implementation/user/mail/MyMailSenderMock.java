package com.goapi.goapi.service.implementation.user.mail;

import com.goapi.goapi.service.interfaces.user.mail.MyMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@ConditionalOnProperty(value = "spring.mail.dev-mode", havingValue = "true", matchIfMissing = true)
public class MyMailSenderMock implements MyMailSender {

    @Override
    public boolean send(String emailTo, String subject, String templateName, Map<String, Object> templateModel, Locale locale) {
        log.info(String.format("Send email to '%s' with subject '%s', template name = '%s', params = %s, locale = ",
            emailTo, subject, templateName, templateModel.toString(), locale.toString()));
        return true;
    }
}
