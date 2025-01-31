package com.example.redisstream.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息实体类
 * 用于封装消息的内容和元数据
 */
@Data
public class Message implements Serializable {
    /**
     * 消息ID
     * 由Redis Stream自动生成
     */
    private String id;

    /**
     * 消息内容
     * 存储实际的消息文本
     */
    private String content;

    /**
     * 消息时间戳
     * 记录消息创建的时间
     */
    private LocalDateTime timestamp;
    
    public Message() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Message(String content) {
        this();
        this.content = content;
    }
}
