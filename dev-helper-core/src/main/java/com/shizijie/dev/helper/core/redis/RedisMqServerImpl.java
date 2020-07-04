package com.shizijie.dev.helper.core.redis;


/**
 * @author shizijie
 * @version 2020-07-04 上午11:48
 */
public class RedisMqServerImpl implements RedisMqServer {


    @Override
    public void pull(String topic) {

    }

    @Override
    public boolean producer(String topic, Object value) {
        return false;
    }
}
