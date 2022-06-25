package com.goapi.goapi.controller.forms.userApi;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateUserApiRequest {

    private final boolean isProtected;
    @NotNull(message = "database id can't be null!")
    private final Integer databaseId;
    @NotNull(message = "tariff id can't be null!")
    private final Integer tariffId;
    @NotBlank(message = "api request name can't be blank!")
    private final String name;

    public CreateUserApiRequest(boolean isProtected, Integer databaseId, Integer tariffId, String name) {
        this.isProtected = isProtected;
        this.databaseId = databaseId;
        this.tariffId = tariffId;
        this.name = name;
    }
}
