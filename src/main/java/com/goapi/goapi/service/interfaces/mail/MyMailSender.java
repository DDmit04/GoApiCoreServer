package com.goapi.goapi.service.interfaces.mail;

import java.util.Locale;
import java.util.Map;

public interface MyMailSender {
    boolean send(String emailTo, String subject, String templateName, Map<String, Object> templateModel, Locale locale);
}
