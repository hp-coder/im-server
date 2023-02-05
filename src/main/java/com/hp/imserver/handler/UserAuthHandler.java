package com.hp.imserver.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.hp.imserver.infrastructure.model.UserContext;
import com.hp.imserver.infrastructure.service.IUserService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

/**
 * @author HP 2023/2/5
 */
@Slf4j
@ChannelHandler.Sharable
public class UserAuthHandler extends ChannelInboundHandlerAdapter {

    public static final AttributeKey<UserContext> USER_CONTEXT_ATTRIBUTE_KEY = AttributeKey.valueOf("USER_CONTEXT_ATTRIBUTE_KEY");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ((msg instanceof FullHttpMessage)) {
            final HttpHeaders headers = ((FullHttpMessage) msg).headers();
            final Optional<String> tokenOptional = Optional.ofNullable(headers.get("token"));
            final String token = tokenOptional.orElse(null);
            final Optional<UserContext> userContext = SpringUtil.getBean(IUserService.class).parseToken(token);
            if (userContext.isPresent()) {
                ctx.channel().attr(USER_CONTEXT_ATTRIBUTE_KEY).set(userContext.get());
                ctx.fireUserEventTriggered(userContext.get());
            } else {
                final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
                sendHttpResponse(ctx, (FullHttpMessage) msg, response);
            }
        }
        super.channelRead(ctx, msg);
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpMessage msg, DefaultFullHttpResponse response) {
        if (response.status().code() != HttpResponseStatus.OK.code()) {
            final ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), StandardCharsets.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
        }
        final ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (!isKeepAlive(response) || response.status().code() != HttpResponseStatus.OK.code()) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
