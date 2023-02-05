package com.hp.imserver;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

/**
 * @author HP 2023/2/5
 */
public class WebsocketTest {

    private WebSocketClient client = new ReactorNettyWebSocketClient();

    @Test
    public void test_web_socket() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token","666" );
        client.execute(
                URI.create("ws://localhost:9999/ws"),
                httpHeaders,
                session -> session.send(Mono.just(session.textMessage("测试连接ws服务")))
                        .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).log())
                        .then()
                )
                .block(Duration.ofSeconds(600));
    }
}
