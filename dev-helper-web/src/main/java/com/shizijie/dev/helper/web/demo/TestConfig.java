package com.shizijie.dev.helper.web.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shizijie
 * @version 2022-04-15 00:05
 */
@Component
public class TestConfig {

    @Value("${test:9}")
    public void setValue(int num){
        System.out.println("set start");
        //System.out.println(TestConfih.name);
        ThreadPoolExecutor value=TestConfih.pool;
        value.setCorePoolSize(num);
        System.out.println("end");
    }


}
