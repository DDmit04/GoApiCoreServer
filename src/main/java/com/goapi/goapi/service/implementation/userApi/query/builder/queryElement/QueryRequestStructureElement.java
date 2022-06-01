package com.goapi.goapi.service.implementation.userApi.query.builder.queryElement;

import com.goapi.goapi.domain.model.userApi.request.RequestArgumentType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class QueryRequestStructureElement {

    private String value;
    private final RequestArgumentType argumentType;
    private final String name;

    public QueryRequestStructureElement(String name, String value, RequestArgumentType argumentType) {
        this.value = value;
        this.argumentType = argumentType;
        this.name = name;
    }

    public QueryRequestStructureElement(String name, RequestArgumentType argumentType) {
        this.value = "";
        this.argumentType = argumentType;
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
