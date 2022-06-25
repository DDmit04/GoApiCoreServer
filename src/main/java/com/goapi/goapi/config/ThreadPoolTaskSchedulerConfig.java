package com.goapi.goapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadPoolTaskSchedulerConfig {

    @Value("${thread.pool-size}")
    private int poolSize;
    @Value("${string.thread-prefix}")
    private String threadPrefix;

    @Bean
    public ThreadPoolTaskScheduler AppServiceTasksScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix(threadPrefix);
        return threadPoolTaskScheduler;
    }
}