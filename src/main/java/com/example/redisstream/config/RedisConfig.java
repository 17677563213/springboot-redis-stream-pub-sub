package com.example.redisstream.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import java.time.Duration;

/**
 * Redis配置类
 * 用于配置Redis连接和Stream消息监听容器
 */
@Configuration
public class RedisConfig {

    /**
     * 配置Redis连接工厂
     * 使用Lettuce作为Redis客户端
     * @return RedisConnectionFactory Redis连接工厂实例
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    /**
     * 配置RedisTemplate
     * 用于执行Redis操作的模板类
     * @param connectionFactory Redis连接工厂
     * @return RedisTemplate Redis操作模板
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    /**
     * 配置Stream消息监听容器选项
     * 用于处理Redis Stream的消息订阅
     * @return StreamMessageListenerContainerOptions Stream消息监听容器选项
     */
    @Bean
    public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> streamMessageListenerContainerOptions() {
        return StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))  // 轮询超时时间
                .targetType(String.class)
                .build();
    }

    /**
     * 配置Stream消息监听容器
     * 用于处理Redis Stream的消息订阅
     * @param connectionFactory Redis连接工厂
     * @param options Stream消息监听容器选项
     * @return StreamMessageListenerContainer Stream消息监听容器
     */
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options) {
        return StreamMessageListenerContainer.create(connectionFactory, options);
    }
}
