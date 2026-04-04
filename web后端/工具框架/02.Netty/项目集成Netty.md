# Netty 项目集成

## 1. 创建 NettyWebSocketStarter 启动类

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class NettyWebSocketStarter implements Runnable {

    // 创建两个线程组，一个用于接收客户端连接，一个用于处理客户端请求
    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup(2);

    private final WebSocketHandler webSocketHandler;
    private final NettyProperties nettyProperties;

    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            // 添加日志处理器，用于调试
                            pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                            // 对http协议的支持，使用http协议的解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 聚合解码 heepRequest/HttpContent/LastHttpContent,保证接受到heep的完整性
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            // 心跳 long readerIdleTime 读超时，即测试端一定事件内未接受到被测试段消息，则断开连接
                            pipeline.addLast(new IdleStateHandler(600, 0, 0, TimeUnit.SECONDS));
                            // 自定义 添加心跳处理器，用于检测客户端是否存活
                            pipeline.addLast(new HeartBeatHandler());
                            /**
                             * 构建 WebSocket 服务器协议配置对象
                             * 配置 WebSocket 握手、帧大小限制、超时等核心参数
                             */
                            WebSocketServerProtocolConfig wsConfig = WebSocketServerProtocolConfig.newBuilder()
                                    // 设置 WebSocket 路径，从配置中读取（如：/websocket）
                                    .websocketPath(nettyProperties.getPath())
                                    // 启用路径前缀匹配，允许 /websocket/* 形式的 URL 都能匹配
                                    .checkStartsWith(true)
                                    // 设置最大帧载荷长度为 65536 字节（64KB），超过此大小的消息将被拒绝
                                    .maxFramePayloadLength(65536)
                                    // 设置握手超时时间为 10000 毫秒（10 秒），超时未完成握手则关闭连接
                                    .handshakeTimeoutMillis(10000L)
                                    // 允许 WebSocket 扩展，支持客户端协商扩展功能（如压缩）
                                    .allowExtensions(true)
                                    .build();
                            // 添加 WebSocket 协议处理器，负责处理 HTTP 到 WebSocket 的升级握手
                            pipeline.addLast(new WebSocketServerProtocolHandler(wsConfig));
                            // 添加自定义业务处理器，处理 WebSocket 消息的读写事件
                            pipeline.addLast(webSocketHandler);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap
                    .bind(nettyProperties.getPort())
                    .sync();
            log.info("Netty WebSocket 服务初始化完成... 端口:{}", nettyProperties.getPort());
            log.info("Netty WebSocket 请求url: ws://{}:{}{}", nettyProperties.getIp(), nettyProperties.getPort(), nettyProperties.getPath());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
```

## 2. 创建 NettyProperties 配置类

```java
@Data
@Component
@ConfigurationProperties(prefix = "netty")
@ConditionalOnProperty(prefix = "netty",value = "ip")
public class NettyProperties {
    private String ip;
    private Integer port;
    private String path;
}
```

## 3. 创建 WebSocketHandler 处理器类

```java
@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final RedisUtil redisUtil;
    private final ParseUtil parseUtil;
    private final RedisComponent redisComponent;
    private final ChannelContextUtils channelContextUtils;

    /**
     * 处理 WebSocket 文本消息读取事件
     * 当客户端发送文本消息时触发，从 Channel 属性中获取用户 ID 并记录消息日志
     *
     * @param ctx Channel 处理上下文，包含当前连接通道信息和处理器引用
     * @param textWebSocketFrame 接收到的文本 WebSocket 帧对象，封装了客户端发送的文本消息内容
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 获取当前连接的 Channel 对象，代表与客户端的通信通道
        Channel channel = ctx.channel();
        // 通过 Channel ID 生成唯一的属性键，获取存储在该 Channel 上的用户 ID 属性
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        // 从属性容器中读取之前存储的用户 ID
        String userId = attribute.get();
        log.debug("用户 id：{} 的消息：{}", userId, textWebSocketFrame.text());
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 处理 WebSocket 握手完成事件，建立用户连接上下文
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("WebSocket 握手完成");
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;

            String url = complete.requestUri();

            try {
                // 校验用户信息是否有效，无效则关闭连接（认证失败）
                TokenUserDTO tokenUserDTO = parseUtil.parseUrl(url);
                if (ObjectUtil.isNull(tokenUserDTO)) {
                    log.error("WebSocket 认证失败：无效的token或参数");
                    ctx.channel().close();
                    return;
                }
                // 将用户 UID 与当前 Channel 绑定，建立用户会话上下文，用于后续消息推送
                channelContextUtils.addContext(tokenUserDTO.getUid(), ctx.channel());
                log.info("WebSocket 连接认证成功，用户ID：{}", tokenUserDTO.getUid());
            } catch (Exception e) {
                log.error("WebSocket 认证过程中发生异常：{}", e.getMessage());
                ctx.channel().close();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的连接加入... 远程地址: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("有连接断开...");
        channelContextUtils.removeContext(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("WebSocket 连接异常: ", cause);
        ctx.close();
    }
}
```

### SimpleChannelInboundHandler<TextWebSocketFrame> 详解

**类的作用**：
- `SimpleChannelInboundHandler` 是 Netty 提供的简化版入站处理器，专门用于处理特定类型的消息
- 泛型参数 `<TextWebSocketFrame>` 指定该处理器只接收 WebSocket 文本帧类型的数据
- 继承该类后，只需重写 `channelRead0()` 方法即可处理业务逻辑，无需手动释放资源

**核心优势**：
1. **自动资源管理**：处理完消息后自动调用 `ReferenceCountUtil.release()` 释放 ByteBuf，避免内存泄漏
2. **类型安全**：通过泛型限定只处理 `TextWebSocketFrame` 类型，其他类型消息会被自动忽略
3. **代码简洁**：相比 `ChannelInboundHandlerAdapter`，减少了样板代码和类型判断逻辑

**生命周期方法**：
- `channelRead0()`：处理接收到的文本消息（必须实现）
- `channelActive()`：客户端连接建立时触发
- `channelInactive()`：客户端断开连接时触发
- `exceptionCaught()`：发生异常时触发，通常用于记录日志和关闭连接
- `userEventTriggered()`：处理用户自定义事件，如 WebSocket 握手完成、心跳超时等

**注意事项**：
- 如果需要在多个 Channel 间共享 Handler 实例，必须添加 `@ChannelHandler.Sharable` 注解
- `channelRead0()` 方法中不应执行耗时操作，避免阻塞 EventLoop 线程
- 如需异步处理消息，应将业务逻辑提交到独立的线程池执行


## 4. 创建 HeartBeatHandler 工具类

```java
@Slf4j
public class HeartBeatHandler extends ChannelDuplexHandler {

    // 处理用户自定义事件的回调方法。当有特定事件触发时，Netty 会自动调用这个方法。
     @Override
     public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

         if (evt instanceof IdleStateEvent) {
             IdleStateEvent event = (IdleStateEvent) evt;

             switch (event.state()) {
                 // 读空闲：超过指定时间没有收到对方消息
                 case READER_IDLE:
                     Channel channel = ctx.channel();
                     Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));

                     String userId = attribute.get();
                     log.debug("客户端用户{}读超时，可能已断开连接", userId);
                     ctx.close();
                     break;
                 case WRITER_IDLE:
                     // 写空闲：超过指定时间没有发送消息
                     log.debug("服务端写超时，发送心跳包");
                     // 创建并发送 WebSocket Ping 帧（心跳包）
                     // Ping 帧是 WebSocket 协议内置的心跳机制，用于：
                     // 1. 检测客户端连接是否仍然存活
                     // 2. 防止防火墙或路由器因连接长时间无数据而自动断开
                     // 3. 保持 TCP 连接活跃，避免中间网络设备认为连接已失效
                     // 4. 客户端收到 Ping 后会自动回复 Pong 帧（RFC 6455 标准）
                     ctx.writeAndFlush(new PingWebSocketFrame());
                     break;
                 case ALL_IDLE:
                     // 读写都空闲
                     log.debug("读写都空闲");
                     break;
             }
         } else {
             // 调用父类方法，将事件传递给 Pipeline 中的下一个 Handler
             super.userEventTriggered(ctx, evt);
         }
     }
}
```

### ChannelDuplexHandler 详解

**类的定义**：
- `ChannelDuplexHandler` 是 Netty 提供的**双工处理器**，既能处理入站（Inbound）事件，也能处理出站（Outbound）事件
- "Duplex" 意为双工，表示该 Handler 可以同时拦截读操作和写操作

**与 SimpleChannelInboundHandler 的区别**：
| 特性 | SimpleChannelInboundHandler | ChannelDuplexHandler |
|------|---------------------------|---------------------|
| 处理方向 | 仅入站（读取数据） | 双向（读写均可拦截） |
| 典型场景 | 业务消息处理 | 心跳检测、日志记录、流量控制 |
| 方法重写 | channelRead0() | write()/flush()/read() 等 |

**常用方法**：
- **入站方法**：
  - `channelRead()`：处理接收到的数据
  - `channelActive()`：连接激活时触发
  - `channelInactive()`：连接断开时触发
  - `userEventTriggered()`：处理用户自定义事件（如心跳超时）

- **出站方法**：
  - `write()`：拦截写操作，可在发送前修改或过滤数据
  - `flush()`：拦截刷新操作
  - `close()`：拦截关闭操作

**在心跳检测中的应用**：
- 通过 `IdleStateHandler` 触发 `IdleStateEvent` 事件
- `ChannelDuplexHandler` 捕获该事件后：
  - **读空闲**（READER_IDLE）：客户端长时间未发消息，判定为异常连接，主动关闭
  - **写空闲**（WRITER_IDLE）：服务端长时间未发消息，主动发送 Ping 帧保持连接活跃
  - **读写空闲**（ALL_IDLE）：双向都无数据传输，可执行自定义逻辑

**注意事项**：
- 如果重写了出站方法（如 `write()`），必须调用 `super.write()` 或 `ctx.write()` 将数据传递给下一个 Handler，否则数据不会发送
- 双工 Handler 适合放在 Pipeline 中间位置，既能监控入站数据，也能控制出站数据



## 5. 发消息

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelContextUtils {

    private static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ChannelGroup> GROUP_CONTEXT_MAP = new ConcurrentHashMap<>();
    
    {
        userChannel.writeAndFlush(
                new TextWebSocketFrame(JSONUtil.toJsonPrettyStr(messageSendDto))
        );
        
        channelGroup.writeAndFlush(
                new TextWebSocketFrame(JSONUtil.toJsonPrettyStr(messageSendDto))
        );
    }
}
```

## 6. 启动 Netty

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitConfig implements ApplicationRunner {

    private final DataSource dataSource;
    private final NettyWebSocketStarter nettyWebSocketStarter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("数据源初始化中...");
            dataSource.getConnection();
            log.info("Netty WebSocket 服务启动中...");
            new Thread(nettyWebSocketStarter).start();
        } catch (SQLException e) {
            log.error("数据库配置错误，请检查数据库配置");
        } catch (Exception e) {
            log.error("Netty Websocket 启动失败...");
        }
    }
}
```