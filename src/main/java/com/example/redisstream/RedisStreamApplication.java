package com.example.redisstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Redis Stream 消息系统应用程序入口
 * 
 * 该应用程序实现了基于Redis Stream的消息发布订阅系统
 * 主要功能包括：
 * 1. 消息的发布（Producer）
 * 2. 消息的订阅（Consumer）
 * 3. 消费者组管理
 * 4. 消息的持久化存储
 */
@SpringBootApplication
public class RedisStreamApplication {

    /**
     * 应用程序入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(RedisStreamApplication.class, args);
    }
}
