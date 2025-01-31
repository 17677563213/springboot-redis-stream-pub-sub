package com.example.redisstream.controller;

import com.example.redisstream.model.Message;
import com.example.redisstream.service.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 消息控制器
 * 提供REST API接口用于发送消息
 */
@Slf4j
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    /**
     * 消息生产者服务
     */
    private final MessageProducer messageProducer;

    /**
     * 构造函数
     * @param messageProducer 消息生产者服务
     */
    public MessageController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    /**
     * 发送消息的REST接口
     * POST /api/messages
     * @param message 消息对象，包含消息内容
     * @return 返回消息ID
     */
    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        try {
            String messageId = messageProducer.sendMessage(message);
            return ResponseEntity.ok(messageId);
        } catch (Exception e) {
            log.error("发送消息时发生错误: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("发送消息失败: " + e.getMessage());
        }
    }
}
