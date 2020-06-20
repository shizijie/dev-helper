package com.shizijie.dev.helper.web.leetcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author shizijie
 * @version 2020-04-22 下午2:16
 */
public class Test {
    private static final int poolSize=Runtime.getRuntime().availableProcessors()*2;

    public static ExecutorService pool= Executors.newFixedThreadPool(poolSize*2);


    @Lock
    public  String startTask(List<String> data,String type){
        Future future=pool.submit(new TestThread(data));
        Object result=getResult(future);
        return result==null?"":result.toString();
    }


    public Object getResult(Future future){
        while (true){
            if(!future.isCancelled()&&future.isDone()){
                break;
            }
            //刷新有效时间
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Object result=null;
        try {
            result=future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
