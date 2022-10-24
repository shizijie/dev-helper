package com.shizijie.dev.helper.web.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shizijie
 * @version 2022-04-15 00:08
 */
public class TestConfih {
    public static ThreadPoolExecutor pool=build();

    private static ThreadPoolExecutor build() {
        ThreadPoolExecutor executor=new ThreadPoolExecutor(4,4,60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),new ThreadPoolExecutor.DiscardOldestPolicy());
        System.out.println("start a thread pool");
        return executor;
    }
}
