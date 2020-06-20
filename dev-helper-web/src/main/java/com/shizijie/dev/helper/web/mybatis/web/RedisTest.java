package com.shizijie.dev.helper.web.mybatis.web;

import com.alibaba.fastjson.JSON;
import com.shizijie.dev.helper.core.api.user.RedisMqHandler;
import com.shizijie.dev.helper.web.mybatis.web.dto.ListDataEnumDTO;
import org.springframework.stereotype.Component;

/**
 * @author shizijie
 * @version 2020-06-20 下午4:44
 */
@Component("redisMqHandler")
public class RedisTest extends RedisMqHandler{

    @Override
    public void consumer(String topic, Object value) {
        String thread=Thread.currentThread().getName();
        System.out.println(thread+" topic: "+topic);
        ListDataEnumDTO dto=parseObject(value,ListDataEnumDTO.class);
        System.out.println(thread+" value: "+JSON.toJSON(dto));

    }
}
