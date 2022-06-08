package com.goapi.goapi.config;

import com.goapi.goapi.exception.DiscoverySererServiceConnectionException;
import com.goapi.goapi.exception.DiscoverySererServiceIsOfflineException;
import com.goapi.goapi.service.interfaces.grpc.DiscoverServerHealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscoveryServerChecker implements CommandLineRunner {

    private final DiscoverServerHealthCheckService discoverServerHealthCheckService;
    @Value("${grpc.checkAlive}")
    private boolean checkDiscoveryServer;

    @Override
    public void run(String... args) {
        if(checkDiscoveryServer) {
            boolean serverAlive = true;
            try {
                boolean databaseServiceIsAlive = discoverServerHealthCheckService.isDiscoverServiceServing();
                serverAlive = databaseServiceIsAlive;
            } catch (io.grpc.StatusRuntimeException e) {
                throw new DiscoverySererServiceConnectionException(e);
            }
            if(serverAlive) {
                throw new DiscoverySererServiceIsOfflineException();
            }
        }
    }
}