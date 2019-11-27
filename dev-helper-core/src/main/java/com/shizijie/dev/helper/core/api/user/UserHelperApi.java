package com.shizijie.dev.helper.core.api.user;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author shizijie
 * @version 2019-11-20 下午3:37
 */
@Validated
public interface UserHelperApi {
    @NotBlank(message = "返回值user不能为空！") String getUser(String userType);
}
