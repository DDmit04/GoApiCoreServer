package com.goapi.goapi.domain.dto.appServiceobject.userApi.summary;

import lombok.Data;
import org.springframework.http.HttpMethod;

import java.io.Serializable;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class SummaryUserApiRequestDto implements Serializable {
    private final Integer id;
    private final String requestName;
    private final String requestTemplate;
    private final HttpMethod httpMethod;
    private final String requestUrl;

}
