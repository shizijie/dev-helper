package com.shizijie.dev.helper.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shizijie
 * @version 2020-06-14 下午11:38
 */
public class Consumer  {

    public void onMessage(String message, String topic) {
        System.out.println("我是sub,监听: "+topic+",我收到消息: "+message);
    }

    public void onMessage2(String message, String topic) {
        System.out.println("我是sub2222,监听: "+topic+",我收到消息: "+message);
    }

//    @Override
//    public void onMessage(Message message, byte[] bytes) {
//        System.out.println("message "+redisTemplate.getValueSerializer().serialize(message.getBody()));
//        System.out.println("topic "+redisTemplate.getStringSerializer().serialize(message.getChannel()));
//    }
}
