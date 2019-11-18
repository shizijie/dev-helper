package com.shizijie.dev.helper.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author shizijie
 * @version 2019-11-15 下午5:25
 */
@Getter
@AllArgsConstructor
public enum DataEnum {
    UUID("UUID","uuid"),
    RONDOM_NUM("RONDOM_NUM","随机数"),
    FIXED_VALUE("FIXED_VALUE","固定值"),
    SQL_VALUE("SQL_VALUE","db字段值");
    public String code;
    public String name;
}
