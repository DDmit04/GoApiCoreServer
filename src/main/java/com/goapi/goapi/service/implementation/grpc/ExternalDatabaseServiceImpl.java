package com.goapi.goapi.service.implementation.grpc;

import com.common.DatabaseIdConnectionsAllowRequest;
import com.common.DatabaseIdPasswordRequest;
import com.common.DatabaseIdRequest;
import com.common.ResponseResult;
import com.common.SendQueryRequest;
import com.common.SendQueryResponse;
import com.common.UpdateDatabaseSizeRequest;
import com.discover.CreateDatabaseOnDiscoverRequest;
import com.discover.DatabaseStatsResponse;
import com.discover.ExternalDatabaseServiceGrpc;
import com.discover.GrpcDatabaseType;
import com.example.DatabaseType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.goapi.goapi.domain.dto.database.DatabaseStatsDto;
import com.goapi.goapi.service.interfaces.grpc.ExternalDatabaseService;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
@Profile("!dev")
public class ExternalDatabaseServiceImpl implements ExternalDatabaseService {

    private final ObjectMapper mapper;
    @GrpcClient("ExternalDatabaseGrpcService")
    private ExternalDatabaseServiceGrpc.ExternalDatabaseServiceBlockingStub externalDatabaseServiceStub;

    @Override
    public DatabaseStatsDto getDatabaseStats(Integer dbId) {
        DatabaseIdRequest databaseIdRequest = DatabaseIdRequest.newBuilder()
            .setDbId(dbId).build();
        DatabaseStatsResponse result = externalDatabaseServiceStub.getDatabaseStats(databaseIdRequest);
        DatabaseStatsDto stats = new DatabaseStatsDto(
            result.getLocation(),
            result.getDatabaseAndUsername(),
            result.getCurrentSize(),
            result.getFillPercent(),
            result.getAllowConnections()
        );
        return stats;
    }

    @Override
    public DatabaseStatsDto createExternalDatabase(long maxSizeBytes, DatabaseType type, Integer dbId, String password) {
        CreateDatabaseOnDiscoverRequest createDatabaseRequest = CreateDatabaseOnDiscoverRequest.newBuilder()
            .setDatabaseType(GrpcDatabaseType.valueOf(type.toString()))
            .setPassword(password)
            .setDbId(dbId)
            .setSize(maxSizeBytes)
            .build();
        DatabaseStatsResponse response = externalDatabaseServiceStub.createDatabase(createDatabaseRequest);
        String location = response.getLocation();
        String databaseAndUsername = response.getDatabaseAndUsername();
        long currentSize = response.getCurrentSize();
        float fillPercent = response.getFillPercent();
        boolean allowConnections = response.getAllowConnections();
        DatabaseStatsDto databaseStatsDto = new DatabaseStatsDto(location, databaseAndUsername, currentSize, fillPercent, allowConnections);
        return databaseStatsDto;
    }

    @Override
    public boolean dropExternalDatabase(Integer dbId) {
        DatabaseIdRequest databaseIdRequest = DatabaseIdRequest.newBuilder()
            .setDbId(dbId).build();
        ResponseResult result = externalDatabaseServiceStub.dropDatabase(databaseIdRequest);
        return result.getResult();
    }

    @Override
    public DatabaseStatsDto resetExternalDatabase(Integer dbId, String password) {
        DatabaseIdPasswordRequest resetDatabaseRequest = DatabaseIdPasswordRequest.newBuilder()
            .setDbId(dbId)
            .setNewPassword(password)
            .build();
        DatabaseStatsResponse response = externalDatabaseServiceStub.resetDatabase(resetDatabaseRequest);
        DatabaseStatsDto stats = new DatabaseStatsDto(
            response.getLocation(),
            response.getDatabaseAndUsername(),
            response.getCurrentSize(),
            response.getFillPercent(),
            response.getAllowConnections()
        );
        return stats;
    }

    @Override
    public boolean updateExternalDatabasePassword(Integer dbId, String newPassword) {
        DatabaseIdPasswordRequest resetDatabasePasswordRequest = DatabaseIdPasswordRequest.newBuilder()
            .setDbId(dbId)
            .setNewPassword(newPassword)
            .build();
        ResponseResult result = externalDatabaseServiceStub.updateDatabasePassword(resetDatabasePasswordRequest);
        return result.getResult();
    }

    @Override
    public boolean changeExternalDatabaseTariff(Integer dbId, long newSize) {
        UpdateDatabaseSizeRequest updateDatabaseSizeRequest = UpdateDatabaseSizeRequest.newBuilder()
            .setDbId(dbId)
            .setNewDatabaseSize(newSize)
            .build();
        ResponseResult result = externalDatabaseServiceStub.updateDatabaseSize(updateDatabaseSizeRequest);
        return result.getResult();
    }


    @Override
    public JsonNode sendQuery(Integer dbId, String finalTemplate) {
        SendQueryRequest request = SendQueryRequest.newBuilder()
            .setDbId(dbId)
            .setQuery(finalTemplate)
            .build();
        SendQueryResponse response = externalDatabaseServiceStub.sendQuery(request);
        try {
            Value structData = response
                .getData()
                .getFieldsMap()
                .get("data");
            String jsonString = JsonFormat.printer().print(structData);
            ArrayNode result = (ArrayNode) mapper.readTree(jsonString);
            return result;
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean forbidExternalDatabaseConnections(Integer dbId) {
        return updateAllowConnections(dbId, false);
    }

    @Override
    public boolean allowExternalDatabaseConnections(Integer dbId) {
        return updateAllowConnections(dbId, true);
    }

    private boolean updateAllowConnections(Integer dbId, boolean value) {
        DatabaseIdConnectionsAllowRequest request = DatabaseIdConnectionsAllowRequest.newBuilder()
            .setDbId(dbId)
            .setAllow(value)
            .build();
        ResponseResult responseResult = externalDatabaseServiceStub.updateDatabaseAllowConnections(request);
        return responseResult.getResult();
    }
}
