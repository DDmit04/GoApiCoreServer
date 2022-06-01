package com.goapi.goapi.service.implementation.userApi.query.builder.queryElement;

import com.goapi.goapi.domain.model.userApi.request.RequestArgumentType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class ArgQueryRequestStructureElement extends QueryRequestStructureElement {
    public ArgQueryRequestStructureElement(String name, RequestArgumentType argumentType) {
        super(name, argumentType);
    }
}
