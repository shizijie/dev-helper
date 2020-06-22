package com.shizijie.dev.helper.core.api.user;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author shizijie
 * @version 2020-06-20 下午4:31
 */
public abstract class RedisMqHandler{
    public static final String TOPIC="REDIS_MQ_HANDLER";

    public static final String RUNING="_RUNING";

    public static final long EXPIRE_SECOND=60L;

    private static final int OFFSET=-1;

    private static final int SIZE=-2;

    private final ScriptSource PUSH=new ResourceScriptSource(new ClassPathResource("redis/push.lua"));

    private final ScriptSource LOCK=new ResourceScriptSource(new ClassPathResource("redis/lock.lua"));

    private Gson gson=new Gson();
    @Autowired
    private RedisTemplate redisTemplate;
    @AllArgsConstructor
    @Data
    class Message{
        private String topic;
        private Object value;
    }

    private final Long SUCCESS=1L;


    public boolean producer(String topic,Object value){
        if(StringUtils.isBlank(topic)||value==null){
            return false;
        }
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(PUSH);
        defaultRedisScript.setResultType(Long.class);
        Object result=redisTemplate.execute(defaultRedisScript, Arrays.asList(topic,TOPIC), OFFSET,SIZE,value,gson.toJson(new Message(topic,value)));
        return SUCCESS.equals(result);
    }
    public void pull(String value){
        Message message=gson.fromJson(value, Message.class);
        boolean needUpdate=false;
        try {
            needUpdate=lock(message.getTopic()+RUNING,EXPIRE_SECOND);
            while (true){

                Integer total= (Integer) redisTemplate.opsForHash().get(message.getTopic(),String.valueOf(SIZE));
                if(total==null){
                    break;
                }
                Integer index= (Integer) redisTemplate.opsForHash().get(message.getTopic(),String.valueOf(OFFSET));
                if(index==null){
                    break;
                }
                if(total>index){
                    Object val=redisTemplate.opsForHash().get(message.getTopic(),String.valueOf(index-1));
                    //consumer(message.getTopic(),gson.fromJson(gson.toJson(message.getValue()),Object.class));
                    consumer(message.getTopic(),val);
                    System.out.println("-------------------end---index:  "+index);
                    redisTemplate.opsForHash().increment(message.getTopic(),String.valueOf(OFFSET),1);
                }else{
                    break;
                }
                if(needUpdate){
                    redisTemplate.expire(message.getTopic()+RUNING,EXPIRE_SECOND, TimeUnit.SECONDS);
                }
            }
        } finally {
            if(needUpdate){
                redisTemplate.delete(message.getTopic()+RUNING);
            }
        }

    }

    public abstract void consumer(String topic,Object value);

    public <T>T parseObject(Object value,Class<T> clazz){
        return gson.fromJson(gson.toJson(value),clazz);
    }

    public boolean cleanTopic(@NotNull String topic){
        if(lock(topic+RUNING,EXPIRE_SECOND)){
            redisTemplate.delete(topic+RUNING);
            redisTemplate.delete(topic);
            return true;
        }else{
            return false;
        }
    }

    public boolean lock(@NotNull String key, String value, long expireSecond){
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(LOCK);
        defaultRedisScript.setResultType(Long.class);
        Object result=redisTemplate.execute(defaultRedisScript,Collections.singletonList(key), value,String.valueOf(expireSecond));
        return SUCCESS.equals(result);
    }

    public boolean lock(@NotNull String key,long expireSecond){
        return lock(key,String.valueOf(SUCCESS),expireSecond);
    }
}
