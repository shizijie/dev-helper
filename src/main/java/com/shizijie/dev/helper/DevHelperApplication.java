package com.shizijie.dev.helper;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shizijie
 * @version 2019-11-10 上午10:21
 */
@SpringBootApplication
public class DevHelperApplication {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(DevHelperApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}