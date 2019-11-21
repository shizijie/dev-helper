package com.shizijie.dev.helper.annotation;

import com.shizijie.dev.helper.config.AnnotationConfiguration;
import com.shizijie.dev.helper.config.DevHelperConfiguration;
import com.shizijie.dev.helper.user.service.UserHelperService;
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
@Import({DevHelperConfiguration.class, AnnotationConfiguration.class})
public @interface EnableDevHelper {
    /** 获取用户信息实现类 */
    Class<? extends UserHelperService> value();
}
