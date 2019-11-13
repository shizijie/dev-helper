package com.shizijie.dev.helper.mybatis.web.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author shizijie
 * @version 2019-11-11 下午3:45
 */
@Data
public class GetTableColumnsVO {
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
}
