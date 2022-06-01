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

    private final DatabaseStatsDto databaseStats;

    public DatabaseDto(Integer id, String name, Date createdAt, DatabaseType databaseType, DatabaseStatsDto databaseStats) {
        super(id, name, createdAt, databaseType);
        this.databaseStats = databaseStats;
    }
}
