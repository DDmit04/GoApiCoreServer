package com.goapi.goapi.controller.forms.database;

import com.example.DatabaseType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class CreateDatabaseRequest {

    @NotBlank(message = "db name can't be empty!")
    private String dbName;
    @PositiveOrZero(message = "tariff id must be positive long!")
    @NotNull(message = "tariff id can't be null!")
    private Integer tariffId;
    @NotNull(message = "db type can't be null!")
    private DatabaseType databaseType;

    public CreateDatabaseRequest(String dbName, Integer tariffId, DatabaseType databaseType) {
        this.dbName = dbName;
        this.tariffId = tariffId;
        this.databaseType = databaseType;
    }
}
