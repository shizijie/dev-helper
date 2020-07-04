package com.shizijie.dev.helper.core.redis;


import com.shizijie.dev.helper.core.annotation.RedisTopic;
import org.springframework.stereotype.Service;

/**
 * @author shizijie
 * @version 2020-07-04 上午11:48
 */
@Service
public class RedisMqServerImpl implements RedisMqServer {


    @Override
    public void pull(String topic) {

    }

    @Override
    @RedisTopic("BBBB")
    public boolean producer(String topic, Object value) {
        System.out.println(topic+"===   SERVER   ===="+value);
        return false;
    }
}
