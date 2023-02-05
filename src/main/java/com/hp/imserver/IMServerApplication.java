package com.hp.imserver;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author HP 2023/2/5
 */
@EnableDiscoveryClient
@EnableSpringUtil
@EnableAsync
@SpringBootApplication
public class IMServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMServerApplication.class, args);
    }
}
