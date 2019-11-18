package com.shizijie.dev.helper.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shizijie
 * @version 2019-11-15 下午5:25
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum DataEnum {
    UUID("UUID","uuid"),
    RONDOM_NUM("RONDOM_NUM","随机数"),
    FIXED_VALUE("FIXED_VALUE","固定值"),
    SQL_VALUE("SQL_VALUE","db字段值");
    protected String code;
    protected String name;
    private static final Map<String,DataEnum> MAP=new HashMap<>(4);
    static {
        for(DataEnum item:DataEnum.values()){
            MAP.put(item.code,item);
        }
    }
    public static DataEnum getByCode(String code){
        return MAP.get(code);
    }
}
