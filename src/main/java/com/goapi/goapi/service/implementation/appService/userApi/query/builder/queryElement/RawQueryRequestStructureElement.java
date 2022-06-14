package com.goapi.goapi.service.implementation.appService.userApi.query.builder.queryElement;

import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class RawQueryRequestStructureElement extends QueryRequestStructureElement {

    public RawQueryRequestStructureElement(String value) {
        super("", value, RequestArgumentType.STRING);
    }
}
