package com.goapi.goapi.controller.forms.api;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateUserApiRequest {

    private boolean isProtected;
    private Integer databaseId;
    private String name;

    public CreateUserApiRequest(boolean isProtected, Integer databaseId, String name) {
        this.isProtected = isProtected;
        this.databaseId = databaseId;
        this.name = name;
    }
}
