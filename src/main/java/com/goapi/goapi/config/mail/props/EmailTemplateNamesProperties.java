package com.goapi.goapi.config.mail.props;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email.template.name")
public class EmailTemplateNamesProperties {
  private String passwordReset;
  private String passwordChanged;
  private String accountCreated;
  private String accountConfirmed;
  private String emailChange;
}
