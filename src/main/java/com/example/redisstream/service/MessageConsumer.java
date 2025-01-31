package com.example.redisstream.service;

import com.example.redisstream.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息消费者服务
 * 负责从Redis Stream中消费消息并进行处理
 */
@Slf4j
@Service
public class MessageConsumer {

    /**
     * Redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Stream消息监听容器
     */
    private final StreamMessageListenerContainer<String, ObjectRecord<String, String>> container;

    /**
     * Stream键名
     */
    private static final String STREAM_KEY = "message-stream";

    /**
     * 消费者组名称
     */
    private static final String CONSUMER_GROUP = "message-group";

    /**
     * 构造函数
     * @param redisTemplate Redis操作模板
     * @param container Stream消息监听容器
     */
    public MessageConsumer(RedisTemplate<String, Object> redisTemplate,
                         StreamMessageListenerContainer<String, ObjectRecord<String, String>> container) {
        this.redisTemplate = redisTemplate;
        this.container = container;
    }

    /**
     * 初始化方法
     * 在服务启动时自动执行
     * 负责创建Stream和消费者组，并启动消息监听
     */
    @PostConstruct
    public void init() {
        try {
            // 检查Stream是否存在
            Boolean hasKey = redisTemplate.hasKey(STREAM_KEY);
            if (Boolean.FALSE.equals(hasKey)) {
                // 如果Stream不存在，创建一个带有初始消息的Stream
                Map<String, String> initMessage = new HashMap<>();
                initMessage.put("init", "init");
                redisTemplate.opsForStream().add(StreamRecords.newRecord()
                    .in(STREAM_KEY)
                    .ofMap(initMessage));
                log.info("创建Stream: {}", STREAM_KEY);
            }

            // 检查消费者组是否存在并创建
            try {
                redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0"), CONSUMER_GROUP);
                log.info("创建消费者组: {}", CONSUMER_GROUP);
            } catch (Exception e) {
                log.info("消费者组已存在: {}", CONSUMER_GROUP);
            }

            // 配置消费者
            Consumer consumer = Consumer.from(CONSUMER_GROUP, "consumer-1");
            StreamOffset<String> streamOffset = StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed());

            // 启动监听
            container.receive(consumer, streamOffset, new StreamListener<String, ObjectRecord<String, String>>() {
                @Override
                public void onMessage(ObjectRecord<String, String> message) {
                    try {
                        log.info("收到消息: {}", message.getValue());
                        // 处理消息的业务逻辑
                        
                        // 确认消息已处理
                        redisTemplate.opsForStream().acknowledge(STREAM_KEY, CONSUMER_GROUP, message.getId());
                    } catch (Exception e) {
                        log.error("处理消息时发生错误: {}", e.getMessage());
                    }
                }
            });
            container.start();
            log.info("消息消费者启动成功");
        } catch (Exception e) {
            log.error("初始化消息消费者时发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }
}
