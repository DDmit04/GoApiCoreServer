package com.goapi.goapi.service.grpc;

import com.common.DatabaseIdRequest;
import com.discover.DatabaseLocationResponse;
import com.discover.ExternalDatabaseServiceStatsGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
public class ExternalDatabaseLocationService {

    @GrpcClient("ExternalDatabaseLocationGrpcService")
    private ExternalDatabaseServiceStatsGrpc.ExternalDatabaseServiceStatsBlockingStub externalDatabaseLocationServiceStub;
    public String getDatabaseLocation(Integer dbId) {
        DatabaseIdRequest databaseIdRequest = DatabaseIdRequest.newBuilder()
            .setDbId(dbId).build();
        DatabaseLocationResponse result = externalDatabaseLocationServiceStub.getDatabaseLocation(databaseIdRequest);
        return result.getLocation();
    }

}
