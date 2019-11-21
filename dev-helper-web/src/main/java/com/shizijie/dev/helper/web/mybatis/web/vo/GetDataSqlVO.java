package com.shizijie.dev.helper.web.mybatis.web.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-18 下午3:01
 */
@Data
public class GetDataSqlVO {
    @NotBlank(message = "数据库连接不能为空！")
    private String url;
    @NotBlank(message = "数据库帐号不能为空！")
    private String username;
    @NotBlank(message = "数据库密码不能为空！")
    private String pwd;
    @NotBlank(message = "数据库连接类型不能为空！")
    private String driver;
    @NotBlank(message = "表名不能为空！")
    private String tableName;
    @NotNull(message = "数量不能为空！")
    @Min(1)
    @Max(10000)
    private Integer number;

    private List<String> columnName;

    private List<String> columnType;

    private List<String> dictType;

    private List<String> dictValue;
}
