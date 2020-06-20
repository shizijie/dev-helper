package com.shizijie.dev.helper.web.test;

import java.util.concurrent.Callable;

/**
 * @author shizijie
 * @version 2020-05-27 下午4:07
 */
public class Task implements Callable {
    private  long time;

    private String str;
    public Task(long time,String str){
        this.time=time;
        this.str=str;
    }

    @Override
    public Object call() throws Exception {
        Thread.sleep(time);
        return str;
    }
}
