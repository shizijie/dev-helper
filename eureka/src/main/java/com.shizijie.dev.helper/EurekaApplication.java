package com.shizijie.dev.helper;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author shizijie
 * @version 2020-05-29 上午9:30
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(EurekaApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
