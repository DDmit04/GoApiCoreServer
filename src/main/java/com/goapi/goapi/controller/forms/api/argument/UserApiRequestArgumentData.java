package com.goapi.goapi.controller.forms.api.argument;

import com.goapi.goapi.domain.model.userApi.request.RequestArgumentType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserApiRequestArgumentData {

    private String argName;
    private RequestArgumentType requestArgumentType;

    public UserApiRequestArgumentData(String argName, RequestArgumentType requestArgumentType) {
        this.argName = argName;
        this.requestArgumentType = requestArgumentType;
    }
}
