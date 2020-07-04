package com.shizijie.dev.helper.core.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shizijie
 * @version 2020-07-04 上午9:07
 */
@AllArgsConstructor
@Getter
public enum ConsumerEnum {
    OFFSET("1","消费offset"),
    QUEUE("2","消费queue"),
    QUEUE_BAK("3","消费queue_bak");
    private String code;
    private String msg;
}
