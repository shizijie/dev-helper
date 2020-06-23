package com.shizijie.dev.helper.core.api.user;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    public static final long EXPIRE_SECOND=60L;

    private static final int OFFSET=-1;

    private static final int SIZE=-2;

    private static final int POOL_SIZE=Runtime.getRuntime().availableProcessors()*2;

    private final ScriptSource PUSH=new ResourceScriptSource(new ClassPathResource("redis/push.lua"));

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


    public boolean producer(String topic,Object value){
        if(StringUtils.isBlank(topic)||value==null){
            return false;
        }
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(PUSH);
        defaultRedisScript.setResultType(Long.class);
        Object result=redisTemplate.execute(defaultRedisScript, Arrays.asList(topic,TOPIC,String.valueOf(OFFSET),String.valueOf(SIZE)),value,gson.toJson(new Message(topic,value)));
        return SUCCESS.equals(result);
    }
    public void pull(String value){
        Message message=gson.fromJson(value, Message.class);
        String lockKey=message.getTopic()+RUNING+getIpAndPort();
        boolean needUpdate=lock(lockKey,EXPIRE_SECOND);
        if(!needUpdate){
            System.out.println("<<<<<<<<<< out "+value);
            //系统正在执行中！
            return;
        }
        System.out.println(">>>>>>>>>>>>>>>>> topic+"+value);
        try {
            while (true){
                if(checkTopicRuning(message.topic)!=null){
                    int num=THREAD_POOL.getCorePoolSize()-THREAD_POOL.getActiveCount();
                    if(num==0){
                        sleep(lockKey);
                        continue;
                    }
                    //消费开始
                    List<Future> result=new ArrayList<>(4);
                    for(int i=1;i<=num;i++){
                        Integer index=checkTopicRuning(message.topic);
                        if(index!=null){
                            Object val=redisTemplate.opsForHash().get(message.getTopic(),String.valueOf(index));
                            //consumer(message.getTopic(),gson.fromJson(gson.toJson(message.getValue()),Object.class));
                            result.add(THREAD_POOL.submit(()->{
                                consumer(message.getTopic(),val);
                            }));
                            //消费结束
                            redisTemplate.opsForHash().increment(message.getTopic(),String.valueOf(OFFSET),1);
                        }
                    }
                    if(!CollectionUtils.isEmpty(result)){
                        for(Future future:result){
                            while (true){
                                if(future.isDone()&&!future.isCancelled()){
                                    future.get();
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
            }
        }
        System.out.println("-------------------end---index:  "+value);

    }

    private void sleep(String key) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(50);
        redisTemplate.expire(key,EXPIRE_SECOND, TimeUnit.SECONDS);
    }

    private Integer checkTopicRuning(String topic){
        Integer total= (Integer) redisTemplate.opsForHash().get(topic,String.valueOf(SIZE));
        if(total==null){
            return null;
        }
        Integer index= (Integer) redisTemplate.opsForHash().get(topic,String.valueOf(OFFSET));
        if(index==null){
            return null;
        }
        if(total>index){
            return index-1;
        }
        return null;
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
    public boolean lock(@NotNull String key, String value, long expireSecond){
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(LOCK);
        defaultRedisScript.setResultType(Long.class);
        Object result=redisTemplate.execute(defaultRedisScript,Arrays.asList(key,String.valueOf(expireSecond)), value);
        return SUCCESS.equals(result);
    }

    public boolean lock(@NotNull String key,long expireSecond){
        return lock(key,String.valueOf(LocalDateTime.now()),expireSecond);
    }
}
