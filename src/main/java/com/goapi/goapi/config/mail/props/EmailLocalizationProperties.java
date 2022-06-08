package com.goapi.goapi.config.mail.props;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email.localization")
public class EmailLocalizationProperties {
  private String path;
  private String encoding;
  private int cacheSeconds;
}
