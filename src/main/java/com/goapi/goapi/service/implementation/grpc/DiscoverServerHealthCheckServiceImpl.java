package com.goapi.goapi.service.implementation.grpc;

import com.goapi.goapi.service.interfaces.grpc.DiscoverServerHealthCheckService;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
@Profile("!dev")
public class DiscoverServerHealthCheckServiceImpl implements DiscoverServerHealthCheckService {

    @GrpcClient("grpcHealthService")
    private HealthGrpc.HealthBlockingStub externalDatabaseServiceStub;
    @Value("${my.grpc.client.names.ExternalDatabaseGrpcService}")
    private String databaseServerServiceName;

    @Override
    public boolean isDiscoverServiceServing() {
        HealthCheckRequest request = HealthCheckRequest.newBuilder()
            .setService(databaseServerServiceName)
            .build();
        HealthCheckResponse response = externalDatabaseServiceStub.check(request);
        return response.getStatus() == HealthCheckResponse.ServingStatus.SERVING;
    }

}
