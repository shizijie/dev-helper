package com.shizijie.dev.helper.core.api.user;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.lang.Nullable;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author shizijie
 * @version 2020-06-20 下午4:31
 */
public abstract class RedisMqHandler{
    public static final String TOPIC="REDIS_MQ_HANDLER";

    public static final String RUNING="_RUNING_";

    public static final String RUNING_QUEUE="_RUNING_QUEUE";

    public static final long EXPIRE_SECOND=60L;

    private static final int OFFSET=-1;

    private static final int SIZE=-2;

    private static final int FINISH_NUM=-3;

    private static final int FINISH_FLAG=-4;

    private static final int POOL_SIZE=Runtime.getRuntime().availableProcessors()*2;

    private final ScriptSource PUSH=new ResourceScriptSource(new ClassPathResource("redis/push.lua"));

    private final ScriptSource PULL=new ResourceScriptSource(new ClassPathResource("redis/pull.lua"));

    private final ScriptSource LOCK=new ResourceScriptSource(new ClassPathResource("redis/lock.lua"));

    private final ThreadPoolExecutor THREAD_POOL= new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    private Gson gson=new Gson();
    @Autowired
    private RedisTemplate redisTemplate;
    @AllArgsConstructor
    @Data
    class Message{
        private String topic;
        private Object value;
    }

    private final Long SUCCESS=1L;

    private final Long FINISH_STATUS=-1L;


    public boolean producer(String topic,Object value){
        if(StringUtils.isBlank(topic)||value==null){
            return false;
        }
        Object result=redisTemplate.execute(getRedisScript(PUSH,Long.class), Arrays.asList(topic,TOPIC,String.valueOf(OFFSET),String.valueOf(SIZE)),value,gson.toJson(new Message(topic,value)));
        return SUCCESS.equals(result);
    }
    public void pull(String value){
        Message message=gson.fromJson(value, Message.class);
        if(checkTopicRuning(message.topic,false)==null){
            return;
        }
        String lockKey=message.getTopic()+RUNING+getIpAndPort();
        boolean needUpdate=lock(lockKey,EXPIRE_SECOND);
        if(!needUpdate){
            //系统正在执行中！
            return;
        }
        System.out.println(lockKey+"-------------------start-----:  "+value);
        try {
            while (true){
                if(checkTopicRuning(message.topic,false)!=null){
                    int num=THREAD_POOL.getCorePoolSize()-THREAD_POOL.getActiveCount();
                    if(num==0){
                        sleep(lockKey);
                        continue;
                    }
                    //消费开始
                    Map<Future,Integer> resultMap=new HashMap<>(num);
                    for(int i=1;i<=num;i++){
                        Integer index=checkTopicRuning(message.topic,true);
                        if(index!=null){
                            Object val=redisTemplate.opsForHash().get(message.getTopic(),String.valueOf(index));
                            resultMap.put(THREAD_POOL.submit(()->{
                                consumer(message.getTopic(),val);
                            }),index);
                        }else{
                            break;
                        }
                    }
                    if(!CollectionUtils.isEmpty(resultMap)){
                        for(Future future:resultMap.keySet()){
                            while (true){
                                if(future.isDone()&&!future.isCancelled()){
                                    future.get();
                                    cleanRuningIndex(message.topic,resultMap.get(future));
                                    break;
                                }
                                sleep(lockKey);
                            }
                        }
                    }
                    sleep(lockKey);
                }else{
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if(needUpdate){
                redisTemplate.delete(lockKey);
                System.out.println(lockKey+"-------------------end-----:  "+value);
            }
        }
        checkTopicStatus(message.getTopic());
    }

    private void checkTopicStatus(String topic) {
        Boolean isOver=redisTemplate.hasKey(topic+RUNING_QUEUE);
        System.out.println(isOver);
        if(isOver==null||!isOver){
            //判断是否完成
            //进行中不为空 转进行中
            System.out.println(topic+"=====>结束！！！！");
        }
    }

    private void sleep(String key) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(50);
        redisTemplate.expire(key,EXPIRE_SECOND, TimeUnit.SECONDS);
    }

    private Integer checkTopicRuning(String topic,boolean hincrby){
        Object result=redisTemplate.execute(getRedisScript(PULL,Long.class),Arrays.asList(topic,String.valueOf(SIZE),String.valueOf(OFFSET),RUNING_QUEUE),hincrby? String.valueOf(LocalDateTime.now()):hincrby );
        if(FINISH_STATUS.equals(result)||result==null){
            return null;
        }else{
            return Integer.parseInt(result.toString());
        }
    }

    private void cleanRuningIndex(String topic,Integer index){
        redisTemplate.opsForHash().delete(topic+RUNING_QUEUE,String.valueOf(index));
    }

    public abstract void consumer(String topic,Object value);

    public <T>T parseObject(Object value,Class<T> clazz){
        return gson.fromJson(gson.toJson(value),clazz);
    }

    @Value("${server.port}")
    private String port ;

    private String getIpAndPort(){
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localHost.getHostAddress()+":"+port;
    }

    public boolean cleanTopic(@NotNull String topic){
        Set<String> keys=redisTemplate.keys(topic+RUNING+"*");
        if(CollectionUtils.isEmpty(keys)){
            redisTemplate.delete(topic);
            return true;
        }else{
            return false;
        }
    }

    private DefaultRedisScript getRedisScript(@Nullable ScriptSource scriptSource,@Nullable Class resultType){
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(scriptSource);
        defaultRedisScript.setResultType(resultType);
        return defaultRedisScript;
    }

    public boolean lock(@NotNull String key, String value, long expireSecond){
        Object result=redisTemplate.execute(getRedisScript(LOCK,Long.class),Arrays.asList(key,String.valueOf(expireSecond)), value);
        return SUCCESS.equals(result);
    }

    public boolean lock(@NotNull String key,long expireSecond){
        return lock(key,String.valueOf(LocalDateTime.now()),expireSecond);
    }
}
