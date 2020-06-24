package com.shizijie.dev.helper.web.mybatis.web;

import com.shizijie.dev.helper.core.api.user.RedisMqHandler;
import com.shizijie.dev.helper.web.mybatis.web.dto.ListDataEnumDTO;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author shizijie
 * @version 2020-06-20 下午4:44
 */
@Component("redisMqHandler")
public class RedisTest extends RedisMqHandler{

    @Override
    public void consumer(String topic, Object value) {
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
