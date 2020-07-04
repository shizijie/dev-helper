package com.shizijie.dev.helper.core.annotation;

import com.shizijie.dev.helper.core.config.AnnotationConfiguration;
import com.shizijie.dev.helper.core.config.DevHelperConfiguration;
import com.shizijie.dev.helper.core.api.user.UserHelperApi;
import com.shizijie.dev.helper.core.config.RedisConfiguration;
import com.shizijie.dev.helper.core.redis.RedisMqServerImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shizijie
 * @version 2019-11-20 下午3:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DevHelperConfiguration.class, AnnotationConfiguration.class, RedisConfiguration.class, RedisMqServerImpl.class})
public @interface EnableDevHelper {
    /** 获取用户信息实现类 */
    Class<? extends UserHelperApi> value();
}
