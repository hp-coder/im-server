package com.hp.imserver.infrastructure.session;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * @author HP 2023/2/5
 */
public interface ISessionStore {

    ChannelGroup channelGroup();

    void addChannel(Channel channel);

    void removeChannel(Channel channel);

    Channel findChannel(String id);
}
