package com.shizijie.dev.helper.core.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shizijie
 * @version 2020-07-04 上午10:13
 */
@AllArgsConstructor
@Getter
public enum  TopicEnum {
    OFFSET("-1","偏移量"),
    SIZE("-2","长度"),
    STATUS("-3","状态 1-offset进行中 2-queue进行中 3-queue_bak进行中 0-已完成"),
    CHECK_STATUS("-4","校验status标志，存在-进行过status校验，不存在-有新数据插入"),

    QUEUE("_QUEUE","queue后缀"),
    QUEUE_BAK("_QUEUE_BAK","queue_bak后缀");
    private String code;
    private String msg;

}
