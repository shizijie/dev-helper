package com.shizijie.dev.helper.core.api.user;

import com.google.gson.Gson;
import com.shizijie.dev.helper.core.redis.TopicEnum;
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

    public static final String TOPIC_ACTIVATE="REDIS_MQ_HANDLER_ACTIVATE";

    public static final String RUNING="_RUNING_";

    public static final long EXPIRE_SECOND=60L;

    private static final int POOL_SIZE=Runtime.getRuntime().availableProcessors()*2;

    private final ScriptSource PUSH=new ResourceScriptSource(new ClassPathResource("redis/push.lua"));

    private final ScriptSource PULL=new ResourceScriptSource(new ClassPathResource("redis/pull2.lua"));

    private final ScriptSource LOCK=new ResourceScriptSource(new ClassPathResource("redis/lock.lua"));

    private final ScriptSource DEL=new ResourceScriptSource(new ClassPathResource("redis/commit.lua"));

    private final ScriptSource CHECK=new ResourceScriptSource(new ClassPathResource("redis/check.lua"));

    private final ThreadPoolExecutor THREAD_POOL= new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    private Gson gson=new Gson();
    @Autowired
    private RedisTemplate redisTemplate;
    @AllArgsConstructor
    @Data
    public class Message{
        private String topic;
        private Object value;
    }

    private final Long SUCCESS=1L;

    /**
     * 推送信息
     * @param topic key
     * @param value value
     * @return
     */
    public boolean producer(String topic,Object value){
        if(StringUtils.isBlank(topic)||value==null){
            return false;
        }
        Object result=redisTemplate.execute(getRedisScript(PUSH,Long.class), Arrays.asList(TOPIC,topic, TopicEnum.OFFSET.getCode(),TopicEnum.SIZE.getCode(),TopicEnum.STATUS.getCode(),TopicEnum.CHECK_STATUS.getCode()),value,gson.toJson(new Message(topic,value)));
        return SUCCESS.equals(result);
    }

    /**
     * 拉取信息
     * @param value  json字符串
     */
    public void pull(String value){
        Message message=gson.fromJson(value, Message.class);
        //判断当前status是否存在任务
        if(checkTopicRuning(message.topic,false)==null){
            return;
        }
        String lockKey=message.getTopic()+RUNING+getIpAndPort();
        boolean hasLock=lock(lockKey,EXPIRE_SECOND);
        if(!hasLock){
            //系统正在执行中！
            return;
        }
        //获取本机topic锁
        try {
            exec(message.topic,lockKey);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if(hasLock){
                redisTemplate.delete(lockKey);
            }
        }
        Long check=checkTopicStatus(message.getTopic());
        if(check>0){
            if(!SUCCESS.equals(check)){
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                redisTemplate.opsForHash().delete(message.topic,TopicEnum.CHECK_STATUS.getCode());
                redisTemplate.convertAndSend(TOPIC,value);
                System.out.println("======  发送信息  ====== ： "+value);
            }else{
                //完成！！！
                System.out.println(lockKey+"-------------------end-----:  "+value);
            }
        }
    }

    private void exec(String topic,String lockKey) throws InterruptedException, ExecutionException {
        while (true){
            //判断当前status是否存在任务
            if(checkTopicRuning(topic,false)!=null){
                //判断是否存在空闲线程
                int num=THREAD_POOL.getCorePoolSize()-THREAD_POOL.getActiveCount();
                if(num==0){
                    sleep(lockKey);
                    continue;
                }
                //消费开始
                Map<Future,Integer> resultMap=new HashMap<>(num);
                for(int i=1;i<=num;i++){
                    Integer index=checkTopicRuning(topic,true);
                    if(index!=null){
                        //消费
                        Object val=redisTemplate.opsForHash().get(topic,String.valueOf(index));
                        resultMap.put(THREAD_POOL.submit(()->{
                            consumer(topic,val);
                        }),index);
                    }else{
                        break;
                    }
                }
                if(!CollectionUtils.isEmpty(resultMap)){
                    for(Future future:resultMap.keySet()){
                        while (true){
                            //完成，清除进度
                            if(future.isDone()&&!future.isCancelled()){
                                future.get();
                                cleanRuningIndex(topic,resultMap.get(future));
                                break;
                            }
                            sleep(lockKey);
                        }
                    }
                }else{
                    sleep(lockKey);
                }
            }else{
                break;
            }
        }
    }



    private Long checkTopicStatus(String topic) {
        Object result=redisTemplate.execute(getRedisScript(CHECK,Long.class),Arrays.asList(topic,TopicEnum.QUEUE.getCode(),TopicEnum.QUEUE_BAK.getCode(),TopicEnum.STATUS.getCode(),TopicEnum.CHECK_STATUS.getCode(),topic+RUNING+"*"));
        return result==null?-3L:Long.parseLong(result.toString());
    }

    private void sleep(String key) throws InterruptedException {
        //刷新本机锁
        TimeUnit.MILLISECONDS.sleep(50);
        redisTemplate.expire(key,EXPIRE_SECOND, TimeUnit.SECONDS);
    }

    private Integer checkTopicRuning(String topic,boolean hincrby){
        Object result=redisTemplate.execute(getRedisScript(PULL,Long.class),Arrays.asList(topic,TopicEnum.QUEUE.getCode(),TopicEnum.QUEUE_BAK.getCode(),TopicEnum.OFFSET.getCode(),TopicEnum.SIZE.getCode(),TopicEnum.STATUS.getCode()),hincrby);
        if(result==null||Integer.parseInt(result.toString())<0){
            return null;
        }else{
            return Integer.parseInt(result.toString());
        }
    }

    private void cleanRuningIndex(String topic,Integer index){
        redisTemplate.execute(getRedisScript(DEL,Long.class),Arrays.asList(topic,TopicEnum.QUEUE.getCode(),TopicEnum.QUEUE_BAK.getCode(),String.valueOf(index),TopicEnum.STATUS.getCode()));
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
