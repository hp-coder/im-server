package com.hp.imserver.infrastructure.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HP 2023/2/5
 */
@Component
public class DefaultSessionStore implements ISessionStore {

    private static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final ConcurrentHashMap<String, ChannelId> CHANNEL_ID_CACHE = new ConcurrentHashMap<>();

    @Override
    public ChannelGroup channelGroup() {
        return CHANNELS;
    }

    @Override
    public void addChannel(Channel channel) {
        CHANNELS.add(channel);
        CHANNEL_ID_CACHE.put(channel.id().asLongText(), channel.id());
    }

    @Override
    public void removeChannel(Channel channel) {
        CHANNELS.remove(channel);
        CHANNEL_ID_CACHE.remove(channel.id().asLongText());
    }

    @Override
    public Channel findChannel(String id) {
        return CHANNELS.find(CHANNEL_ID_CACHE.get(id));
    }
}
