package com.goapi.goapi.props.appServiceTimeProps;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
@Component
@ConfigurationProperties(prefix = "time")
public class AppServiceTimeProps {
    private Duration payoutPeriod;
    private Duration deleteDisabledPeriod;
}
