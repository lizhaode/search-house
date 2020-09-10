package com.lizhao.searchhouse.config;

import com.lizhao.searchhouse.subscribe.BeikeSub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisConfig {

    @Autowired
    private LettuceConnectionFactory factory;

    @Bean
    RedisMessageListenerContainer container() {
        RedisMessageListenerContainer c = new RedisMessageListenerContainer();
        c.addMessageListener(new BeikeSub(), ChannelTopic.of("bei_ke"));
        c.setConnectionFactory(factory);
        c.afterPropertiesSet();
        return c;
    }
}
