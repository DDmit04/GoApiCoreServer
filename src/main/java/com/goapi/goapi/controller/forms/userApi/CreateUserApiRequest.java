package com.goapi.goapi.controller.forms.userApi;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateUserApiRequest {

    private final boolean isProtected;
    private final Integer databaseId;
    private final Integer tariffId;
    private final String name;

    public CreateUserApiRequest(boolean isProtected, Integer databaseId, Integer tariffId, String name) {
        this.isProtected = isProtected;
        this.databaseId = databaseId;
        this.tariffId = tariffId;
        this.name = name;
    }
}
