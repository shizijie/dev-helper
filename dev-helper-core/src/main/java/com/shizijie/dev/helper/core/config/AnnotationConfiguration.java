package com.shizijie.dev.helper.core.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author shizijie
 * @version 2019-11-20 下午6:06
 */
@Configuration
public class AnnotationConfiguration implements ImportBeanDefinitionRegistrar{
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        DevHelperConfiguration.setImportingClassMetadata(importingClassMetadata);
    }
}
