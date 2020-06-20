package com.shizijie.dev.helper.web.leetcode;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author shizijie
 * @version 2020-04-22 下午2:19
 */
public class TestThread implements Callable {
    private List<String> data;

    public TestThread(List<String> data){
        this.data=data;
    }


    @Override
    public Object call() throws Exception {
        System.out.println("test");
        Thread[] threads = new Thread[1];
        return StringUtils.join(data,",");
    }
}
