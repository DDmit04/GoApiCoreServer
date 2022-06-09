package com.goapi.goapi.controller.forms.api.argument;

import com.goapi.goapi.domain.model.userApi.request.RequestArgumentType;
import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UpdateApiRequestArgument extends UserApiRequestArgumentData {

    private Integer id;

    public UpdateApiRequestArgument(String argName, RequestArgumentType requestArgumentType, Integer id) {
        super(argName, requestArgumentType);
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateApiRequestArgument)) return false;

        UpdateApiRequestArgument that = (UpdateApiRequestArgument) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
