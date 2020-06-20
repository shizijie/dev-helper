package com.shizijie.dev.helper.web.test;

import com.shizijie.dev.helper.core.api.user.UserHelperApi;

import javax.validation.constraints.NotBlank;

/**
 * @author shizijie
 * @version 2020-06-14 下午11:18
 */
public class UserService implements UserHelperApi {
    @Override
    public @NotBlank(message = "返回值user不能为空！") String getUser(String userType) {
        return "sys";
    }
}
