package com.shizijie.dev.helper.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

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
    public static Connection getConnection(String url,String username,String pwd,String driver){
        Connection connection=null;
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(url,username,pwd);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return connection;
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
