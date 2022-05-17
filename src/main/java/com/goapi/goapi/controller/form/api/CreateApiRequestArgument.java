package com.goapi.goapi.controller.form.api;

import com.goapi.goapi.domain.api.request.RequestArgumentType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateApiRequestArgument {

    private String argName;
    private RequestArgumentType requestArgumentType;

    public CreateApiRequestArgument(String argName, RequestArgumentType requestArgumentType) {
        this.argName = argName;
        this.requestArgumentType = requestArgumentType;
    }
}
