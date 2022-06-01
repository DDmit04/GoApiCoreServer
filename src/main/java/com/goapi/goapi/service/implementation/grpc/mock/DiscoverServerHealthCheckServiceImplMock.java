package com.goapi.goapi.service.implementation.grpc.mock;

import com.goapi.goapi.service.interfaces.grpc.DiscoverServerHealthCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DiscoverServerHealthCheckServiceImplMock implements DiscoverServerHealthCheckService {

    @Value("${my.grpc.client.names.ExternalDatabaseGrpcService}")
    private String databaseServerServiceName;

    @Override
    public boolean isDiscoverServiceServing() {
        log.info(String.format("Check health of grpc service with name '%s'", databaseServerServiceName));
        return true;
    }

}
