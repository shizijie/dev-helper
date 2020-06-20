package com.shizijie.dev.helper.core.api.user;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author shizijie
 * @version 2020-06-20 下午4:31
 */
public abstract class RedisMqHandler{
    public static final String TOPIC="REDIS_MQ_HANDLER";

    private Gson gson=new Gson();
    @Autowired
    private RedisTemplate redisTemplate;
    @AllArgsConstructor
    @Data
    class Message{
        private String topic;
        private Object value;
    }

    public void producer(String topic,Object value){
        //do status
        redisTemplate.convertAndSend(TOPIC, gson.toJson(new Message(topic,value)));
    }
    public void pull(String value){
        //
        Message message=gson.fromJson(value, Message.class);
        consumer(message.getTopic(),gson.fromJson(gson.toJson(message.getValue()),Object.class));
    }

    public abstract void consumer(String topic,Object value);

    public <T>T parseObject(Object value,Class<T> clazz){
        return gson.fromJson(gson.toJson(value),clazz);
    }
}
