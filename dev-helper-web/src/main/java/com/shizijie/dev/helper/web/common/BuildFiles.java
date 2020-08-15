package com.shizijie.dev.helper.web.common;

public interface BuildFiles {
    String BASE_PATH=System.getProperty("user.dir")+"/helper-conf";
    /**
     * SSH配置
     */
    String DEPLOY_PATH=BASE_PATH+"/deploy";
    /**
     * sql生成地址
     */
    String SQL_OUT_PATH=BASE_PATH+"/sqlfile";
    /**
     * java生成地址
     */
    String JAVA_OUT_PATH=BASE_PATH+"/javafile";
    /**
     * 数据库地址
     */
    String DATABASE_PATH=BASE_PATH+"/database";
}
