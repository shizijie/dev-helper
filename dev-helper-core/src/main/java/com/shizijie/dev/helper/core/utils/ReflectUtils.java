package com.shizijie.dev.helper.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.Date;

import static org.springframework.util.ClassUtils.CGLIB_CLASS_SEPARATOR;

/**
 * @author shizijie
 * @version 2019-11-27 下午4:18
 */
@Slf4j
@SuppressWarnings("all")
public class ReflectUtils {

    public static Object getFieldValue(Object object,String fieldName){
        Validate.notNull(object, "Instance must not be null");
        Validate.notBlank(fieldName, "Instance must not be null");
        Object result=null;
        Field field=getField(object,fieldName);
        if(field!=null){
            ReflectionUtils.makeAccessible(field);
            try {
                result=field.get(object);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(),e);
            }
        }
        return result;
    }

    private static Field getField(Object object, String fieldName) {
        Field field=null;
        for(Class<?> clazz=object.getClass();clazz!=Object.class;clazz=clazz.getSuperclass()){
            try {
                field=clazz.getDeclaredField(fieldName);
                break;
            } catch (Exception e) {
                // do nothing
            }
        }
        return field;
    }

    public static void setFieldValue(Object object,String fieldName,Object fieldValue){
        Field field=getField(object,fieldName);
        if(field!=null){
            ReflectionUtils.makeAccessible(field);
            try {
                field.set(object,fieldValue);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(),e);
            }
        }
    }


    public static Class<?> getUserClass(Object instance) {
        Validate.notNull(instance, "Instance must not be null");
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;

    }
}
