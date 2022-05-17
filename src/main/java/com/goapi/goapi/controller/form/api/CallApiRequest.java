package com.goapi.goapi.controller.form.api;

import lombok.Getter;

import java.util.List;
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
