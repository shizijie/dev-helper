package com.shizijie.dev.helper.mybatis.web.dto;

import lombok.Data;

/**
 * @author shizijie
 * @version 2019-11-15 下午4:34
 */
@Data
public class QueryTableInfoDTO {
    private String columnName;
    private String columnType;
    private String columnRemark;
}
