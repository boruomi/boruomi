package com.boruomi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.password}")
    private String redisPassword;
    @Value("${spring.data.redis.port}")
    private Integer redisPort;
    @Value("${spring.data.redis.database}")
    private Integer redisDatabase;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(10);  // 最大空闲连接数
        poolConfig.setMaxTotal(20); // 最大连接数
        poolConfig.setMinIdle(5);   // 最小空闲连接数

        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        factory.setPassword(redisPassword);  // 如果没有密码，则可以忽略
        factory.setDatabase(redisDatabase);   // 默认数据库为0
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        // 设置Key和Value的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // 或者其他序列化方式

        return redisTemplate;
    }
}
