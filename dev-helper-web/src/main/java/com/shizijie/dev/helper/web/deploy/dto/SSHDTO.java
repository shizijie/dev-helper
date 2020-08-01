package com.shizijie.dev.helper.web.deploy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author shizijie
 * @version 2020-08-01 上午10:08
 */
@Data
public class SSHDTO {
    @NotBlank
    private String comment;
    @NotBlank
    private String ip;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
