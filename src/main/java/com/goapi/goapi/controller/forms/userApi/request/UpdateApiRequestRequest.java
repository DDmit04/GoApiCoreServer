package com.goapi.goapi.controller.forms.userApi.request;

import com.goapi.goapi.controller.forms.userApi.argument.UpdateApiRequestArgument;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UpdateApiRequestRequest extends UserApiRequestData {


    @NotNull(message = "api request id can't be null!")
    private final Integer id;
    private final Set<UpdateApiRequestArgument> apiRequestArguments;

    public UpdateApiRequestRequest(String requestName, String requestTemplate, HttpMethod httpMethod, Integer id, Set<UpdateApiRequestArgument> apiRequestArguments) {
        super(requestName, requestTemplate, httpMethod);
        this.id = id;
        this.apiRequestArguments = apiRequestArguments;
    }
}
