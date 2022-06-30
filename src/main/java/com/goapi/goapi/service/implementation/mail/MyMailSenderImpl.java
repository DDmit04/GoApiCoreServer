package com.goapi.goapi.service.implementation.mail;

import com.goapi.goapi.service.interfaces.user.mail.MyMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;

@Service
@ConditionalOnProperty(value = "spring.mail.dev-mode", havingValue = "false")
@RequiredArgsConstructor
public class MyMailSenderImpl implements MyMailSender {

    private final ITemplateEngine emailTemplateEngine;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Value("${email.app-name.value}")
    private String appName;

    @Value("${email.app-name.param-name}")
    private String appNameParamName;
    @Override
    public boolean send(String emailTo, String subject, String templateName, Map<String, Object> templateModel, Locale locale) {
        templateModel.putIfAbsent(appNameParamName, appName);
        try {
            Context thymeleafContext = new Context(locale);
            thymeleafContext.setVariables(templateModel);
            String htmlBody = emailTemplateEngine.process(templateName, thymeleafContext);
            sendHtmlMessage(emailTo, subject, htmlBody);
        } catch (MailException | MessagingException mailException) {
            return false;
        }
        return true;
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

}
