package com.goapi.goapi.props.mailProps;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email.localization.names.subject")
public class EmailLocalizationSubjectNamesProperties {
    private String passwordRecover;
    private String passwordChanged;
    private String accountCreated;
    private String accountConfirmed;
    private String emailChange;
}
