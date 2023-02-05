package com.hp.imserver.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author HP 2023/2/5
 */
@Data
@Component
@ConfigurationProperties("im.server")
public class WebsocketConfig {

    private Integer port;
    private String path;
}
