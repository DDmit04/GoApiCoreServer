package com.goapi.goapi.service.implementation.grpc.mock;

import com.example.DatabaseType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.goapi.goapi.domain.dto.appServiceobject.database.DatabaseStatsDto;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
@Profile("dev" )
@Slf4j
public class ExternalDatabaseServiceImplMock implements ExternalDatabaseService {

    private final ObjectMapper mapper;
    private final DatabaseStatsDto testDatabaseStatsDto = new DatabaseStatsDto(
        "testLoc",
        "testDatabaseUsername",
        0,
        0,
        true
    );

    @Override
    public DatabaseStatsDto getDatabaseStats(Integer dbId) {
        log.info(String.format("Get database stats with id = '%s' grpc", dbId));
        return testDatabaseStatsDto;
    }

    @Override
    public DatabaseStatsDto createExternalDatabase(long maxSizeBytes, DatabaseType type, Integer dbId, String password) {
        log.info(String.format("Create database with id = '%s', maxBytes = '%s', type = '%s', password = '%s' grpc", dbId, maxSizeBytes, type.toString(), password));
        return testDatabaseStatsDto;
    }

    @Override
    public boolean dropExternalDatabase(Integer dbId) {
        log.info(String.format("Drop database with id = '%s' grpc", dbId));
        return true;
    }

    @Override
    public DatabaseStatsDto resetExternalDatabase(Integer dbId, String password) {
        log.info(String.format("Reset database with id = '%s' grpc", dbId));
        return testDatabaseStatsDto;
    }

    @Override
    public boolean updateExternalDatabasePassword(Integer dbId, String newPassword) {
        log.info(String.format("Update database password with id = '%s', newPassword = '%s' grpc", dbId, newPassword));
        return true;
    }

    @Override
    public boolean changeExternalDatabaseTariff(Integer dbId, long newSize) {
        log.info(String.format("Update database tariff with db_id = '%s', new size = '%s' grpc", dbId, newSize));
        return true;
    }


    @Override
    public JsonNode sendQuery(Integer dbId, String finalTemplate) {
        log.info(String.format("Send database query with db_id = '%s', query = '%s' grpc", dbId, finalTemplate));
        ArrayNode result = null;
        try {
            result = (ArrayNode) mapper.readTree("[{\"test\": 123}]");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean forbidExternalDatabaseConnections(Integer dbId) {
        log.info(String.format("Forbid external database connections for db with id = '%s' grpc", dbId));
        return true;
    }

    @Override
    public boolean allowExternalDatabaseConnections(Integer dbId) {
        log.info(String.format("Allow external database connections for db with id = '%s' grpc", dbId));
        return true;
    }
}
