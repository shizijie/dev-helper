package com.shizijie.dev.helper.web;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shizijie
 * @version 2019-11-10 上午10:21
 */
@SpringBootApplication
public class DevHelperWebApplication {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(DevHelperWebApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}