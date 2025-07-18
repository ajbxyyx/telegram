package com.ajbxyyx.config.Netty;



import com.ajbxyyx.config.Netty.Handler.RequestInfoHandler;
import com.ajbxyyx.config.Netty.Handler.WebSocketSharkHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NettyWebSocketServer {
    //处理accept事件            ->   nio
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    //处理read&write事件        ->   nio
    private EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());
    public static final int WEB_SOCKET_PORT = 9090;
    @Autowired
    private RequestInfoHandler requestInfoHandler;
    @Autowired
    private WebSocketSharkHandler webSocketSharkHandler;

    public void run() throws InterruptedException {
        // 服务器启动引导对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.WARN)); // 为 bossGroup 添加 日志处理器

        serverBootstrap.childHandler(
                new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();

                    //30秒客户端没有向服务器发送心跳则关闭连接
                    pipeline.addLast(new IdleStateHandler(30, 0, 0));
                    // 因为使用http协议，所以需要使用http的编码器，解码器
                    pipeline.addLast(new HttpServerCodec());
                    // 以块方式写，添加 chunkedWriter 处理器
                    pipeline.addLast(new ChunkedWriteHandler());
                    /**
                     * 说明：
                     *  1. http数据在传输过程中是分段的，HttpObjectAggregator可以把多个段聚合起来；
                     *  2. 这就是为什么当浏览器发送大量数据时，就会发出多次 http请求的原因
                     */
                    pipeline.addLast(new HttpObjectAggregator(8192));
                    //解析连接的token
                    pipeline.addLast(requestInfoHandler);
                    /**
                     * 说明：
                     *  1. 对于 WebSocket，它的数据是以帧frame 的形式传递的；
                     *  2. 可以看到 WebSocketFrame 下面有6个子类
                     *  3. 浏览器发送请求时： ws://localhost:7000/hello 表示请求的uri
                     *  4. WebSocketServerProtocolHandler 核心功能是把 http协议升级为 ws 协议，保持长连接；
                     *      是通过一个状态码 101 来切换的
                     */
                    pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                    //连接成功 自定义handler ，处理业务逻辑
                    pipeline.addLast(webSocketSharkHandler);



               }
            }
        );
        // 启动服务器，监听端口，阻塞直到启动成功
        serverBootstrap.bind(WEB_SOCKET_PORT).sync();
    }














    /**
     * 启动 ws server
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        run();  //项目启动之初就运行该方法
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        Future<?> future = bossGroup.shutdownGracefully();
        Future<?> future1 = workerGroup.shutdownGracefully();
        future.syncUninterruptibly();
        future1.syncUninterruptibly();
        log.info("关闭 ws server 成功");
    }
}
