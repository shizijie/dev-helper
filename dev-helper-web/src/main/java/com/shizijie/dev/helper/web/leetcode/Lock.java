package com.shizijie.dev.helper.web.leetcode;

/**
 * @author shizijie
 * @version 2020-04-22 下午2:37
 */
public @interface Lock {
    String key() default "";
}
