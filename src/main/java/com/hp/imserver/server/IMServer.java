package com.hp.imserver.server;

import cn.hutool.extra.spring.SpringUtil;
import com.hp.imserver.handler.MessageHandler;
import com.hp.imserver.handler.UserAuthHandler;
import com.hp.imserver.infrastructure.config.IMServerRegister;
import com.hp.imserver.infrastructure.config.WebsocketConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HP 2023/2/5
 */
@Slf4j
public class IMServer {

    public void start() {
        final NioEventLoopGroup boss = new NioEventLoopGroup(1);
        final NioEventLoopGroup workers = new NioEventLoopGroup();
        try {
            final WebsocketConfig websocketConfig = SpringUtil.getBean(WebsocketConfig.class);
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, workers).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("日志处理", new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast("http编解码", new HttpServerCodec());
                    ch.pipeline().addLast(new ChunkedWriteHandler());
                    ch.pipeline().addLast(new HttpObjectAggregator(100 * 1024 * 1024));
                    //When using websocket compression, you need to set allowExtension to true when adding WebSocketServerProtocolHandler and WebSocketClientProtocolHandler.
                    // ch.pipeline().addLast("数据压缩",new WebSocketServerCompressionHandler());
                    ch.pipeline().addLast("用户校验", new UserAuthHandler());
                    ch.pipeline().addLast("websocket协议处理", new WebSocketServerProtocolHandler(websocketConfig.getPath()));
                    ch.pipeline().addLast("消息处理", new MessageHandler());
                }
            });
            final Channel channel = bootstrap.bind(websocketConfig.getPort()).sync().channel();
            log.info("websocket 启动成功，端口号:{},上下文:{}", websocketConfig.getPort(), websocketConfig.getPath());
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("IMServer启动异常", e);
        }
        finally {
            SpringUtil.getBean(IMServerRegister.class).deRegisterFromNacos();
            boss.shutdownGracefully();
            workers.shutdownGracefully();
            log.info("IMServer已关闭");
        }
    }
}
