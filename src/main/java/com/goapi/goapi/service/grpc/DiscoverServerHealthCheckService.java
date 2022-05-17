package com.goapi.goapi.service.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class DiscoverServerHealthCheckService {

    @GrpcClient("grpcHealthService")
    private HealthGrpc.HealthBlockingStub externalDatabaseServiceStub;
    @Value("${my.grpc.client.names.ExternalDatabaseGrpcService}")
    private String databaseServerServiceName;
    @Value("${my.grpc.client.names.ExternalDatabaseLocationGrpcService}")
    private String databaseServerStatsServiceName;

    public boolean isDiscoverServiceServing() {
        HealthCheckRequest request = HealthCheckRequest.newBuilder()
            .setService(databaseServerServiceName)
            .build();
        HealthCheckResponse response = externalDatabaseServiceStub.check(request);
        return response.getStatus() == HealthCheckResponse.ServingStatus.SERVING;
    }

    public boolean isDiscoverStatsServiceServing() {
        HealthCheckRequest request = HealthCheckRequest.newBuilder()
            .setService(databaseServerStatsServiceName)
            .build();
        HealthCheckResponse response = externalDatabaseServiceStub.check(request);
        return response.getStatus() == HealthCheckResponse.ServingStatus.SERVING;
    }

    private HealthGrpc.HealthBlockingStub createServerStub(String url) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(url)
            .usePlaintext()
            .build();
        HealthGrpc.HealthBlockingStub blockingStub = HealthGrpc.newBlockingStub(channel);
        return blockingStub;
    }

}
