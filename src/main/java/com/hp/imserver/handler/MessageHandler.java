package com.hp.imserver.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.hp.imserver.infrastructure.model.UserContext;
import com.hp.imserver.infrastructure.session.ISessionStore;
import com.hp.imserver.infrastructure.session.IUserChannelStore;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * @author HP 2023/2/5
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            final TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            log.info("收到消息：{}", textWebSocketFrame.text());
        }
        final TextWebSocketFrame payload = new TextWebSocketFrame(Instant.now().toEpochMilli() + ":" + "收到消息");
        ctx.channel().writeAndFlush(payload);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final ISessionStore sessionStore = SpringUtil.getBean(ISessionStore.class);
        sessionStore.removeChannel(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        final IUserChannelStore userChannelStore = SpringUtil.getBean(IUserChannelStore.class);
        if (evt instanceof UserContext) {
            log.info("校验通过");
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("完成握手");
            final ISessionStore sessionStore = SpringUtil.getBean(ISessionStore.class);
            sessionStore.addChannel(ctx.channel());
            log.info("客户端加入连接：{}", ctx.channel());
            final UserContext userContext = ctx.channel().attr(UserAuthHandler.USER_CONTEXT_ATTRIBUTE_KEY).get();
            userChannelStore.bindUserToChannel(userContext.getUserId(), ctx.channel());
            log.info("用户-channel绑定成功：{} - {}", userContext.getUserId(), ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }
}
