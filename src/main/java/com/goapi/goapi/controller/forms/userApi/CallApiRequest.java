package com.goapi.goapi.controller.forms.userApi;

import lombok.Getter;

import java.util.Map;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CallApiRequest {

    private Map<String, Object> arguments;

    public CallApiRequest(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public CallApiRequest() {
    }
}
