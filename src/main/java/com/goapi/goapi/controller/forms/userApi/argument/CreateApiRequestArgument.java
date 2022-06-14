package com.goapi.goapi.controller.forms.userApi.argument;

import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateApiRequestArgument extends UserApiRequestArgumentData {

    public CreateApiRequestArgument(String argName, RequestArgumentType requestArgumentType) {
        super(argName, requestArgumentType);
    }
}
