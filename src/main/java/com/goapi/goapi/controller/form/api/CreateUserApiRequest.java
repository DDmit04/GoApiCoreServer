package com.goapi.goapi.controller.form.api;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateUserApiRequest {

    private boolean isProtected;
    private Integer apiTariffId;
    private Integer databaseId;
    private String name;

    public CreateUserApiRequest(boolean isProtected, Integer apiTariffId, Integer databaseId, String name) {
        this.isProtected = isProtected;
        this.apiTariffId = apiTariffId;
        this.databaseId = databaseId;
        this.name = name;
    }
}
