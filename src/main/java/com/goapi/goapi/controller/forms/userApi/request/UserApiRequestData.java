package com.goapi.goapi.controller.forms.userApi.request;

import lombok.Getter;
import org.springframework.http.HttpMethod;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserApiRequestData {

    private final String requestName;
    private final String requestTemplate;
    private final HttpMethod httpMethod;

    public UserApiRequestData(String requestName, String requestTemplate, HttpMethod httpMethod) {
        this.requestName = requestName;
        this.requestTemplate = requestTemplate;
        this.httpMethod = httpMethod;
    }
}
