package com.shizijie.dev.helper.core.config;

import com.shizijie.dev.helper.core.annotation.EnableDevHelper;
import com.shizijie.dev.helper.core.api.user.UserHelperApi;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author shizijie
 * @version 2019-11-20 下午4:00
 */
@Configuration
public class DevHelperConfiguration {
    @Setter
    public static AnnotationMetadata importingClassMetadata;

    @Bean
    public UserHelperApi userHelperService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Map<String,Object> annotationMap=importingClassMetadata.getAnnotationAttributes(EnableDevHelper.class.getName(),true);
        Class clazz=Class.forName((String) annotationMap.get("value"));
        return (UserHelperApi) clazz.newInstance();
    }

}
