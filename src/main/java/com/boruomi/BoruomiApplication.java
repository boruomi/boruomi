package com.boruomi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ServletComponentScan
@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com.boruomi.business.mapper")
public class BoruomiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoruomiApplication.class, args);
    }

}
