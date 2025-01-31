package com.example.redisstream.service;

import com.example.redisstream.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 消息生产者服务
 * 负责将消息发送到Redis Stream
 */
@Slf4j
@Service
public class MessageProducer {

    /**
     * Redis操作模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Stream键名
     */
    private static final String STREAM_KEY = "message-stream";

    /**
     * 构造函数
     * @param redisTemplate Redis操作模板
     */
    public MessageProducer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送消息到Redis Stream
     * @param message 要发送的消息对象
     * @return 返回消息ID
     */
    public String sendMessage(Message message) {
        try {
            // 设置消息时间戳
            message.setTimestamp(LocalDateTime.now());
            
            // 创建Stream记录
            ObjectRecord<String, Message> record = StreamRecords
                .newRecord()
                .in(STREAM_KEY)
                .ofObject(message);

            // 发送消息到Stream
            String messageId = redisTemplate.opsForStream().add(record).getValue();
            log.info("消息发送成功: {}, 消息ID: {}", message, messageId);
            
            return messageId;
        } catch (Exception e) {
            log.error("发送消息时发生错误: {}", e.getMessage());
            throw new RuntimeException("发送消息失败", e);
        }
    }
}
