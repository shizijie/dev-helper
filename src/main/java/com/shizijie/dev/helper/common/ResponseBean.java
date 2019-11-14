package com.shizijie.dev.helper.common;

import lombok.Data;

/**
 * @author shizijie
 * @version 2019-11-11 下午5:38
 */
@Data
public class ResponseBean<T> {
    private String code;
    private String msg;
    private T result;
}
