package com.shizijie.dev.helper.web.mybatis.web;

import com.shizijie.dev.helper.core.annotation.RedisTopic;
import com.shizijie.dev.helper.core.api.user.RedisMqHandler;
import com.shizijie.dev.helper.core.redis.RedisMqServer;
import com.shizijie.dev.helper.web.mybatis.web.dto.ListDataEnumDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shizijie
 * @version 2020-06-20 下午4:44
 */
@Component("redisMqHandler")
public class RedisTest extends RedisMqHandler{
    @Autowired
    private List<RedisMqServer> list;

    @Override
    public void consumer(String topic, Object value) {
        for(RedisMqServer server:list){
            Method[] methods=server.getClass().getDeclaredMethods();
            for(Method method:methods){
                if(method.getAnnotation(RedisTopic.class)!=null){
                    if(method.getAnnotation(RedisTopic.class).value().equals(topic)){
                        server.producer(topic,value);
                    }
                }
            }
        }
        String thread=Thread.currentThread().getName();
        ListDataEnumDTO dto=parseObject(value,ListDataEnumDTO.class);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread+" topic: "+topic+" value: "+dto.getEnumName());
    }
}
