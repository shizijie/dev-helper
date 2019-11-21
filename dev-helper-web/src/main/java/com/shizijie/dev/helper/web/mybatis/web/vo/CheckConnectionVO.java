package com.shizijie.dev.helper.web.mybatis.web.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author shizijie
 * @version 2019-11-15 上午10:28
 */
@Data
public class CheckConnectionVO {
    @NotBlank(message = "数据库连接不能为空！")
    private String url;
    @NotBlank(message = "数据库帐号不能为空！")
    private String username;
    @NotBlank(message = "数据库密码不能为空！")
    private String pwd;
    @NotBlank(message = "数据库连接类型不能为空！")
    private String driver;
}
