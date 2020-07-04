package com.shizijie.dev.helper.core.redis;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shizijie
 * @version 2020-07-04 上午9:04
 */
public interface RedisMqServer {
    int POOL_SIZE=Runtime.getRuntime().availableProcessors()*2;

    ThreadPoolExecutor REDIS_THREAD_POOL= new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());


    void pull(String topic);

    boolean producer(String topic,Object value);

}
