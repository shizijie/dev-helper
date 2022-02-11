package com.shizijie.dev.helper.web.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author shizijie
 * @version 2020-06-14 下午11:20
 */
@RestController
public class TestController {
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/test")
    public String test(String name){
        return (String) redisTemplate.opsForValue().get(name);
    }


    @GetMapping("/test1")
    public Long test1(String name){
        long start=System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            CompletableFuture.runAsync(()->{
                String res=(String) redisTemplate.opsForValue().get(name);
            });
        }
        return System.currentTimeMillis()-start;
    }
}
