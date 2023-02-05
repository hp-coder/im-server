package com.hp.imserver.controller;

import com.hp.imserver.infrastructure.session.ISessionStore;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HP 2023/2/5
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/websocket")
public class WebsocketController {
    private final ISessionStore sessionStore;

    @PostMapping("/send")
    public PayLoad send(@RequestBody PayLoad payLoad) {
        final TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(payLoad.getMessage());
        sessionStore.channelGroup().writeAndFlush(textWebSocketFrame);
        return new PayLoad("发送成功", HttpStatus.OK);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayLoad {
        private String message;
        private Integer code;

        public PayLoad(String message, HttpStatus status) {
            this.message = message;
            this.code = status.value();
        }
    }
}
