package com.goapi.goapi.controller.forms.api.request;

import com.goapi.goapi.controller.forms.api.argument.UpdateApiRequestArgument;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UpdateApiRequestRequest extends UserApiRequestData {

    private final Integer id;
    private final Set<UpdateApiRequestArgument> apiRequestArguments;

    public UpdateApiRequestRequest(String requestName, String requestTemplate, HttpMethod httpMethod, Integer id, Set<UpdateApiRequestArgument> apiRequestArguments) {
        super(requestName, requestTemplate, httpMethod);
        this.id = id;
        this.apiRequestArguments = apiRequestArguments;
    }
}
