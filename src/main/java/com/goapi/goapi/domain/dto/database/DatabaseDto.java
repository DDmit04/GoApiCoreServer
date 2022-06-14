package com.goapi.goapi.domain.dto.database;

import com.example.DatabaseType;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class DatabaseDto extends SummaryDatabaseDto implements Serializable {

    private final boolean allowConnections;
    private final DatabaseStatsDto databaseStats;

    public DatabaseDto(Integer id, String externalName, Date createdAt, DatabaseType databaseType, boolean allowConnections, DatabaseStatsDto databaseStats) {
        super(id, externalName, createdAt, databaseType);
        this.allowConnections = allowConnections;
        this.databaseStats = databaseStats;
    }
}
