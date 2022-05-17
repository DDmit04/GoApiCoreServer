package com.goapi.goapi.config;

import com.goapi.goapi.service.grpc.DiscoverServerHealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {

    private final DiscoverServerHealthCheckService discoverServerHealthCheckService;
    @Value("${my.grpc.client.checkAlive}")
    private boolean checkDiscoveryServer;

    @Override
    public void run(String... args) {
        if(checkDiscoveryServer) {
            boolean serverAlive = true;
            try {
                boolean databaseServiceIsAlive = discoverServerHealthCheckService.isDiscoverServiceServing();
                boolean databaseStatsServiceIsAlive = discoverServerHealthCheckService.isDiscoverStatsServiceServing();
                serverAlive = databaseServiceIsAlive && databaseStatsServiceIsAlive;
            } catch (io.grpc.StatusRuntimeException e) {
                serverAlive = false;
            }
            if (!serverAlive) {
                throw new RuntimeException();
            }
        }
    }
}