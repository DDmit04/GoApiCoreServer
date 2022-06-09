package com.goapi.goapi.controller.forms.api.argument;

import com.goapi.goapi.domain.model.userApi.request.RequestArgumentType;
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
