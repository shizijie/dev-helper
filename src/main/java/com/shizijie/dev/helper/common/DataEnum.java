package com.shizijie.dev.helper.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author shizijie
 * @version 2019-11-15 下午5:25
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum DataEnum {
    TEXT_FIXED("TEXT_FIXED","固定值(字符串)"),
    TEXT_UUID("TEXT_UUID","uuid"),
    TEXT_NUM_RONDOM("TEXT_NUMBER_RONDOM","随机数(字符串)"),
    TEXT_FUNC_CUSTOM("TEXT_FUNC_CUSTOM","DB函数(自定义)"),

    NUMBER_RONDOM("NUMBER_RONDOM","随机数"),
    NUMBER_FIXED("NUMBER_FIXED","固定值(数字)"),

    FUNC_TIMESTAMP("FUNC_TIMESTAMP","DB函数(当前时间)"),
    FUNC_CUSTOM("FUNC_CUSTOM","DB函数(自定义)"),

    SQL_VALUE("SQL_VALUE","db字段值");

    protected String code;
    protected String name;
    private static final Map<String,DataEnum> MAP=new HashMap<>(5);

    private static final int RANDOM_NUMBER_LENGTH=2;
    static {
        for(DataEnum item:DataEnum.values()){
            MAP.put(item.code,item);
        }
    }
    public static DataEnum getByCode(String code){
        return MAP.get(code);
    }

    public static List<String> getDataByCode(String dictType,String dictValue,Connection connection) throws SQLException {
        List<String> result=new ArrayList<>();
        switch (getByCode(dictType)){
            /** ========  字符串  ======== */
            case TEXT_UUID:
                result.add("'"+ UUID.randomUUID().toString().replace("-","").toLowerCase()+"'");
                break;
            case TEXT_FIXED:
                if(StringUtils.isNotBlank(dictValue)){
                    result.add("'"+dictValue+"'");
                }else{
                    result.add("null");
                }
                break;
            case TEXT_NUM_RONDOM:
                result.add("'"+getRandomNumber(dictValue)+"'");
                break;

            /** ========  数字  ======== */
            case NUMBER_RONDOM:
                result.add(getRandomNumber(dictValue));
                break;
            case NUMBER_FIXED:
                if(StringUtils.isNotBlank(dictValue)){
                    result.add(dictValue);
                }else{
                    result.add("null");
                }
                break;
            case TEXT_FUNC_CUSTOM:
                result.add(dictValue);
                break;

            /** ========  函数  ======== */
            case FUNC_TIMESTAMP:
                result.add("current_timestamp(6)");
                break;
            case FUNC_CUSTOM:
                result.add(dictValue);

            /** ========  sql  ======== */
            case SQL_VALUE:
                PreparedStatement ps = connection.prepareStatement(dictValue);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    if(result.size()>10000){
                        throw new SQLException("["+dictValue+"]查询结果超过1W！");
                    }
                    result.add(resultSet.getString(1));
                }
                break;

            default:
                throw new SQLException("枚举["+dictType+"]错误！");
        }
        return result;
    }


    public static String getRandomNumber(String value){
        if(StringUtils.isBlank(value)){
            return new Random().nextInt((int)Math.pow(10,RANDOM_NUMBER_LENGTH))+"";
        }else if(value.indexOf(",")!=-1){
            String[] str=value.split(",");
            Integer start=RANDOM_NUMBER_LENGTH;
            Integer end=RANDOM_NUMBER_LENGTH;
            try {
                start=Integer.parseInt(str[0]);
            } catch (Exception e) {
                start=RANDOM_NUMBER_LENGTH;
            }
            try {
                end=Integer.parseInt(str[1]);
            } catch (Exception e) {
                end=RANDOM_NUMBER_LENGTH;
            }
            return new Random().nextInt((int)Math.pow(10,start))+"."+new Random().nextInt((int)Math.pow(10,end));
        }else{
            Integer num=RANDOM_NUMBER_LENGTH;
            try {
                num=Integer.parseInt(value);
            } catch (Exception e) {
                num=RANDOM_NUMBER_LENGTH;
            }
            return new Random().nextInt((int)Math.pow(10,num))+"";
        }
    }
}
