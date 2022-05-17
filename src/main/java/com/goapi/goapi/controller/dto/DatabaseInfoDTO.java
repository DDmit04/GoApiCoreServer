package com.goapi.goapi.controller.dto;

import com.example.DatabaseType;
import com.goapi.goapi.domain.database.Database;

import java.util.Date;

public record DatabaseInfoDTO(String host, String dbName, Date created, DatabaseType type, long currentSize, long maxSize) {
    public DatabaseInfoDTO(String host, Database db, long currentSize, long maxSize) {
        this(host, db.getName(), db.getCreatedAt(), db.getDatabaseType(), currentSize, maxSize);
    }

    public DatabaseInfoDTO(DatabaseAuthInfo authInfo, Date created, DatabaseType type, long currentSize, long maxSize) {
        this(authInfo.location(), authInfo.dbName(), created, type, currentSize, maxSize);
    }

    public DatabaseInfoDTO(DatabaseAuthInfo authInfo, Database db, long currentSize, long maxSize) {
        this(authInfo.location(), authInfo.dbName(), db.getCreatedAt(), db.getDatabaseType(), currentSize, maxSize);
    }
}
