package com.hp.imserver.infrastructure.config;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.client.naming.NacosNamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author HP 2023/2/5
 */
@Component
@RequiredArgsConstructor
public class IMServerRegister implements SmartInitializingSingleton {

    private final WebsocketConfig webSocketConfig;

    @Value("${spring.cloud.nacos.discovery.server-addr:}")
    private String nacos;
    @Value("${spring.cloud.nacos.discovery.group:}")
    private String group;
    @Value("${spring.cloud.nacos.discovery.namespace:}")
    private String namespace;

    @Override
    public void afterSingletonsInstantiated() {
        try (final InetUtils inetUtils = new InetUtils(new InetUtilsProperties())) {
            final Properties properties = new Properties();
            properties.put(PropertyKeyConst.NAMESPACE, namespace);
            properties.put(PropertyKeyConst.SERVER_ADDR, nacos);
            final NacosNamingService nacosNamingService = new NacosNamingService(properties);
            nacosNamingService.registerInstance("ws", group, inetUtils.findFirstNonLoopbackAddress().getHostAddress(), webSocketConfig.getPort());
        } catch (Exception e) {
            throw new BeanInitializationException("IM服务启动失败", e);
        }
    }

    public void deRegisterFromNacos(){
        try (final InetUtils inetUtils = new InetUtils(new InetUtilsProperties())) {
            final Properties properties = new Properties();
            properties.put(PropertyKeyConst.NAMESPACE, namespace);
            properties.put(PropertyKeyConst.SERVER_ADDR, nacos);
            final NacosNamingService nacosNamingService = new NacosNamingService(properties);
            nacosNamingService.deregisterInstance("ws", group, inetUtils.findFirstNonLoopbackAddress().getHostAddress(), webSocketConfig.getPort());
        } catch (Exception e) {
            throw new BeanInitializationException("IM服务启动失败", e);
        }
    }
}
