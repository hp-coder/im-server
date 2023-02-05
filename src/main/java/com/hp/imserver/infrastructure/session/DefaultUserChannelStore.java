package com.hp.imserver.infrastructure.session;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HP 2023/2/5
 */
@Component
public class DefaultUserChannelStore implements IUserChannelStore {

    private static final ConcurrentHashMap<Long, Channel> USER_CHANNEL = new ConcurrentHashMap<>();


    @Override
    public Optional<Channel> findChannelByUserId(Long userId) {
        return Optional.ofNullable(USER_CHANNEL.get(userId));
    }

    @Override
    public void bindUserToChannel(Long userId, Channel channel) {
        USER_CHANNEL.put(userId, channel);
    }
}
