package com.goapi.goapi.domain.dto.database;

import lombok.Data;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class DatabaseStatsDto {

    private final String location;
    private final String dbAndUsername;
    private final long currentSize;
    private final float fillPercent;

    public DatabaseStatsDto(String location, String dbAndUsername, long currentSize, float fillPercent) {
        this.location = location;
        this.dbAndUsername = dbAndUsername;
        this.currentSize = currentSize;
        this.fillPercent = fillPercent;
    }
}
