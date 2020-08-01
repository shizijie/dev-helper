package com.shizijie.dev.helper.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shizijie
 * @version 2019-11-11 下午7:52
 */
@Slf4j
public class DataSourcesUtils {
    private static String MYSQL_URL="jdbc:mysql://%s?useUnicode=true&characterEncoding=UTF8&useSSL=false";

    private static String POSTGRESQL_URL="jdbc:postgresql://%s";


    public static Connection getConnection(String url,String username,String pwd,String driver){
        Connection connection=null;
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(getUrl(driver,url),username,pwd);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return connection;
    }

    public static String checkConnection(String url,String username,String pwd,String driver){
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
            return "driver is not find!"+e.getMessage();
        }
        try(Connection connection= DriverManager.getConnection(getUrl(driver,url),username,pwd)){
            if(connection==null){
                return "url/user/pwd/driver is error!";
            }
        }catch (SQLException e) {
            log.error(e.getMessage(),e);
            return "url/user/pwd/driver is error!"+e.getMessage();
        }
        return null;
    }


    public static String getUrl(String driver,String url){
        switch (driver){
            case "com.mysql.jdbc.Driver":
                return String.format(MYSQL_URL,url);
            case "org.postgresql.Driver":
                return String.format(POSTGRESQL_URL,url);
                default:
                    return null;
        }
    }

    public static String underlineToCamel(String name){
        if(StringUtils.isBlank(name)){
            return name;
        }
        StringBuffer sb=new StringBuffer(name);
        Matcher mc= Pattern.compile("_").matcher(name);
        int i=0;
        while (mc.find()){
            int position=mc.end()-(i++);
            sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
        }
        return sb.toString().replace(" ","");
    }
}
