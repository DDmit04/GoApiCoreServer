package com.goapi.goapi.controller.forms.api;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateApiRequestRequest {

    private String requestName;
    private String requestTemplate;
    private HttpMethod httpMethod;
    private Set<CreateApiRequestArgument> apiRequestArguments = new LinkedHashSet<>();

    public CreateApiRequestRequest(String requestName, String requestTemplate, HttpMethod httpMethod, Set<CreateApiRequestArgument> apiRequestArguments) {
        this.requestName = requestName;
        this.requestTemplate = requestTemplate;
        this.httpMethod = httpMethod;
        this.apiRequestArguments = apiRequestArguments;
    }
}
