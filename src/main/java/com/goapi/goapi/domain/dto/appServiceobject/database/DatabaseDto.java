package com.goapi.goapi.domain.dto.appServiceobject.database;

import com.example.DatabaseType;
import com.goapi.goapi.domain.dto.appServiceobject.AppServiceObjectStatusDto;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseDto extends SummaryDatabaseDto implements Serializable {

    private final DatabaseStatsDto databaseStats;

    public DatabaseDto(Integer id, String name, Date createdAt, AppServiceObjectStatusDto appServiceObjectStatusDto, DatabaseType databaseType, DatabaseStatsDto databaseStats) {
        super(id, name, createdAt, appServiceObjectStatusDto, databaseType);
        this.databaseStats = databaseStats;
    }
}
