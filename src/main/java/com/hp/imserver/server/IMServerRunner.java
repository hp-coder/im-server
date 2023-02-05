package com.hp.imserver.server;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author HP 2023/2/5
 */
@Async
@Component
public class IMServerRunner implements SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        final IMServer imServer = new IMServer();
        imServer.start();
    }
}
