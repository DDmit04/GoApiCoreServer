package com.goapi.goapi.service.grpc;

import com.common.DatabaseIdRequest;
import com.common.ResetDatabaseRequest;
import com.common.ResponseResult;
import com.common.SendQueryRequest;
import com.common.SendQueryResponse;
import com.common.UpdateDatabasePasswordRequest;
import com.common.UpdateDatabaseSizeRequest;
import com.discover.CreateDatabaseOnDiscoverRequest;
import com.discover.DatabaseOnDiscoverResponse;
import com.discover.ExternalDatabaseServiceGrpc;
import com.discover.GrpcDatabaseType;
import com.example.DatabaseType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.goapi.goapi.controller.dto.DatabaseAuthInfo;
import com.goapi.goapi.domain.database.DatabaseTariff;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class ExternalDatabaseService {

    private final ObjectMapper mapper;
    @GrpcClient("ExternalDatabaseGrpcService")
    private ExternalDatabaseServiceGrpc.ExternalDatabaseServiceBlockingStub externalDatabaseServiceStub;

    public DatabaseAuthInfo createExternalDatabase(DatabaseTariff tariff, DatabaseType type, Integer dbId, String password) {
        CreateDatabaseOnDiscoverRequest createDatabaseRequest = CreateDatabaseOnDiscoverRequest.newBuilder()
            .setDatabaseType(GrpcDatabaseType.valueOf(type.toString()))
            .setPassword(password)
            .setDbId(dbId)
            .setSize(tariff.getMaxSizeBytes())
            .build();
        DatabaseOnDiscoverResponse response = externalDatabaseServiceStub.createDatabase(createDatabaseRequest);
        String location = response.getLocation();
        String username = response.getUsername();
        String databaseName = response.getDatabaseName();
        DatabaseAuthInfo authInfo = new DatabaseAuthInfo(dbId, location, username, databaseName);
        return authInfo;
    }

    public boolean dropExternalDatabase(Integer dbId) {
        DatabaseIdRequest databaseIdRequest = DatabaseIdRequest.newBuilder()
            .setDbId(dbId).build();
        ResponseResult result = externalDatabaseServiceStub.dropDatabase(databaseIdRequest);
        return result.getResult();
    }

    public DatabaseAuthInfo resetExternalDatabase(Integer dbId, String password) {
        ResetDatabaseRequest resetDatabaseRequest = ResetDatabaseRequest.newBuilder()
            .setDbId(dbId)
            .setNewPassword(password)
            .build();
        DatabaseOnDiscoverResponse response = externalDatabaseServiceStub.resetDatabase(resetDatabaseRequest);
        String location = response.getLocation();
        String username = response.getUsername();
        String databaseName = response.getDatabaseName();
        DatabaseAuthInfo authInfo = new DatabaseAuthInfo(dbId, location, username, databaseName);
        return authInfo;
    }

    public boolean updateExternalDatabasePassword(Integer dbId, String newPassword) {
        UpdateDatabasePasswordRequest resetDatabasePasswordRequest = UpdateDatabasePasswordRequest.newBuilder()
            .setDbId(dbId)
            .setNewPassword(newPassword)
            .build();
        ResponseResult result = externalDatabaseServiceStub.updateDatabasePassword(resetDatabasePasswordRequest);
        return result.getResult();
    }

    public boolean changeExternalDatabaseTariff(Integer dbId, long newSize) {
        UpdateDatabaseSizeRequest updateDatabaseSizeRequest = UpdateDatabaseSizeRequest.newBuilder()
            .setDbId(dbId)
            .setNewDatabaseSize(newSize)
            .build();
        ResponseResult result = externalDatabaseServiceStub.updateDatabaseSize(updateDatabaseSizeRequest);
        return result.getResult();
    }


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
            ArrayNode result = (ArrayNode)mapper.readTree(jsonString);
            return result;
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
