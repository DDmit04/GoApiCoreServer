package com.goapi.goapi.controller.forms.api.request;

import com.goapi.goapi.controller.forms.api.argument.CreateApiRequestArgument;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateApiRequestRequest extends UserApiRequestData{

    private final List<CreateApiRequestArgument> apiRequestArguments;

    public CreateApiRequestRequest(String requestName, String requestTemplate, HttpMethod httpMethod, List<CreateApiRequestArgument> apiRequestArguments) {
        super(requestName, requestTemplate, httpMethod);
        this.apiRequestArguments = apiRequestArguments;
    }
}
