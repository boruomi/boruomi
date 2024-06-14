package com.boruomi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);        // 核心线程数
        executor.setMaxPoolSize(5);         // 最大线程数
        executor.setQueueCapacity(500);     // 队列容量
        executor.setThreadNamePrefix("AsyncThread-");
        executor.setWaitForTasksToCompleteOnShutdown(true); // 等待所有任务完成后再关闭
        executor.setAwaitTerminationSeconds(60);            // 等待任务完成的时间
        executor.initialize();
        return executor;
    }
}
