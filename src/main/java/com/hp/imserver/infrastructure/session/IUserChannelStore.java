package com.hp.imserver.infrastructure.session;

import io.netty.channel.Channel;

import java.util.Optional;

/**
 * @author HP 2023/2/5
 */
public interface IUserChannelStore {

    Optional<Channel> findChannelByUserId(Long userId);

    void bindUserToChannel(Long userId, Channel channel);
}
