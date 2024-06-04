package com.boruomi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com.boruomi.business.mapper")
public class BoruomiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoruomiApplication.class, args);
    }

}
