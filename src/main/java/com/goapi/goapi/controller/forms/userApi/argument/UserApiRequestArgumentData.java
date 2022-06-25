package com.goapi.goapi.controller.forms.userApi.argument;

import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserApiRequestArgumentData {

    @NotBlank(message = "api request argument name can't be blank!")
    private String argName;

    @NotNull(message = "api request argument type can't be null!")
    private RequestArgumentType requestArgumentType;

    public UserApiRequestArgumentData(String argName, RequestArgumentType requestArgumentType) {
        this.argName = argName;
        this.requestArgumentType = requestArgumentType;
    }
}
