package com.goapi.goapi.controller.forms.userApi.request;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserApiRequestData {

    @NotBlank(message = "api request name can't be blank!")
    private final String requestName;
    @NotBlank(message = "api request template can't be blank!")
    private final String requestTemplate;
    @NotNull(message = "api request http method can't be null!")
    private final HttpMethod httpMethod;

    public UserApiRequestData(String requestName, String requestTemplate, HttpMethod httpMethod) {
        this.requestName = requestName;
        this.requestTemplate = requestTemplate;
        this.httpMethod = httpMethod;
    }
}
