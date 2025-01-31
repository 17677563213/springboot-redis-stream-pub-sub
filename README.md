# Redis Stream 消息系统

这是一个基于 Spring Boot 和 Redis Stream 实现的消息发布订阅系统。该系统提供了可靠的消息传递机制，支持消息的持久化存储和消费者组模式。

## 功能特点

- 消息发布：支持发送消息到 Redis Stream
- 消息订阅：支持通过消费者组订阅和处理消息
- 消息持久化：利用 Redis Stream 的特性实现消息持久化
- 消费者组：支持多个消费者并行处理消息
- 错误处理：完善的错误处理和日志记录机制
- REST API：提供 RESTful 接口进行消息发送

## 技术栈

- Spring Boot 2.7.0
- Spring Data Redis
- Redis 6.x
- Java 8
- Maven
- Lombok

## 系统架构

系统主要包含以下组件：

1. **消息生产者 (MessageProducer)**
   - 负责将消息发送到 Redis Stream
   - 支持消息的序列化和时间戳记录

2. **消息消费者 (MessageConsumer)**
   - 负责从 Redis Stream 中消费消息
   - 支持消费者组模式
   - 自动创建 Stream 和消费者组
   - 支持消息确认机制

3. **REST API 控制器 (MessageController)**
   - 提供 HTTP 接口发送消息
   - 支持错误处理和响应封装

4. **Redis 配置 (RedisConfig)**
   - 配置 Redis 连接
   - 配置 Stream 监听容器
   - 配置消息序列化

## API 接口

### 发送消息
```http
POST /api/messages
Content-Type: application/json

{
    "content": "消息内容"
}
```

响应示例：
```json
{
    "1738303043216-0"  // 消息ID
}
```

## 配置说明

配置文件 `application.properties` 包含以下主要配置项：

```properties
# 应用服务端口
server.port=8081

# Redis连接配置
spring.redis.host=localhost
spring.redis.port=6379

# Redis连接池配置
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
spring.redis.lettuce.pool.max-wait=-1ms
```

## 快速开始

### 环境要求
- JDK 8 或以上
- Maven 3.6 或以上
- Redis 6.x 或以上

### 安装步骤

1. 克隆项目：
```bash
git clone [项目地址]
```

2. 进入项目目录：
```bash
cd redis-stream-demo
```

3. 编译项目：
```bash
mvn clean install
```

4. 运行项目：
```bash
mvn spring-boot:run
```

### 使用示例

1. 发送消息：
```bash
curl -X POST http://localhost:8081/api/messages \
     -H "Content-Type: application/json" \
     -d '{"content":"这是一条测试消息"}'
```

## 项目结构

```
src/main/java/com/example/redisstream/
├── RedisStreamApplication.java        # 应用程序入口
├── config/
│   └── RedisConfig.java              # Redis配置类
├── controller/
│   └── MessageController.java        # REST API控制器
├── model/
│   └── Message.java                  # 消息实体类
└── service/
    ├── MessageConsumer.java          # 消息消费者服务
    └── MessageProducer.java          # 消息生产者服务
```

## 注意事项

1. 确保 Redis 服务器已启动并可访问
2. 默认配置使用本地 Redis（localhost:6379）
3. 消费者组会自动创建，无需手动创建
4. 消息处理失败会记录到错误日志

## 性能考虑

- 使用 Lettuce 作为 Redis 客户端，支持异步操作
- 配置了连接池优化性能
- 支持批量消息处理
- 使用消费者组实现负载均衡

## 扩展建议

1. 添加消息优先级支持
2. 实现消息重试机制
3. 添加消息过期策略
4. 实现消息过滤功能
5. 添加监控和告警机制

## 故障排除

常见问题：

1. 连接 Redis 失败
   - 检查 Redis 服务是否启动
   - 验证连接配置是否正确

2. 消息发送失败
   - 检查 Redis 连接状态
   - 查看错误日志获取详细信息

3. 消费者组创建失败
   - 确保 Redis 版本支持 Stream
   - 检查 Redis 权限设置

## 日志说明

系统使用 SLF4J + Logback 进行日志记录，主要记录：

- 消息发送和接收情况
- Stream 和消费者组的创建
- 错误和异常信息
- 系统启动和关闭状态

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进项目。在提交代码时请：

1. 遵循现有的代码风格
2. 添加适当的测试用例
3. 更新相关文档

## 许可证

[MIT License](LICENSE)
