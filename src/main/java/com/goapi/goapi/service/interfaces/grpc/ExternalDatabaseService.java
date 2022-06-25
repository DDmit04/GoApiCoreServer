package com.goapi.goapi.service.interfaces.grpc;

import com.example.DatabaseType;
import com.fasterxml.jackson.databind.JsonNode;
import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseStatsDto;

public interface ExternalDatabaseService {

    DatabaseStatsDto getDatabaseStats(Integer dbId);
    DatabaseStatsDto createExternalDatabase(long maxSizeBytes, DatabaseType type, Integer dbId, String password);
    boolean dropExternalDatabase(Integer dbId);
    DatabaseStatsDto resetExternalDatabase(Integer dbId, String password);
    boolean updateExternalDatabasePassword(Integer dbId, String newPassword);
    boolean changeExternalDatabaseTariff(Integer dbId, long newSize);
    JsonNode sendQuery(Integer dbId, String finalTemplate);
    boolean forbidExternalDatabaseConnections(Integer dbId);
    boolean allowExternalDatabaseConnections(Integer dbId);
}
