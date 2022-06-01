package com.goapi.goapi.domain.dto.database;

import com.example.DatabaseType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class SummaryDatabaseDto implements Serializable {
    private final Integer id;
    @NotBlank(message = "db name can't be blank!")
    private final String externalName;
    @NotNull(message = "db creation time can't be null!")
    private final Date createdAt;
    private final DatabaseType databaseType;

    public SummaryDatabaseDto(Integer id, String externalName, Date createdAt, DatabaseType databaseType) {
        this.id = id;
        this.externalName = externalName;
        this.createdAt = createdAt;
        this.databaseType = databaseType;
    }
}
