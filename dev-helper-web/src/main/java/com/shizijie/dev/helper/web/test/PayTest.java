package com.shizijie.dev.helper.web.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author shizijie
 * @version 2020-05-27 下午4:03
 */
public class PayTest {
    private final static ExecutorService THREAD_POOL=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    private final static long PASS_TIME=10;

    private final static String[] TYPES={"1","2","3","4","5"};
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start=System.currentTimeMillis();
        signal();
        System.out.println("耗时："+(System.currentTimeMillis()-start));
        start=System.currentTimeMillis();
        doubleTask();
        System.out.println("耗时："+(System.currentTimeMillis()-start));

    }

    public static void signal() throws InterruptedException {
        List<String> result=new ArrayList<>();
        for(String str:TYPES){
            Thread.sleep(PASS_TIME);
            result.add(str);
        }
        System.out.println(result);
    }

    public static void doubleTask() throws InterruptedException, ExecutionException {
        List<Future> list=new ArrayList<>();
//        for(String str:TYPES){
//            list.add(THREAD_POOL.submit(new Task(PASS_TIME,str)));
//        }
        for(String str:TYPES){
            list.add(THREAD_POOL.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    Thread.sleep(PASS_TIME);
                    return str;
                }
            }));
        }
        List<String> result=new ArrayList<>();
        for(Future future:list){
            while (true){
                if(!future.isCancelled()&&future.isDone()){
                    if(future.get()!=null){
                        String s= (String) future.get();
                        if(s!=null){
                            result.add(s+"");
                        }
                    }
                    break;
                }
                Thread.sleep(50);
            }
        }
        System.out.println(result);
    }
}
