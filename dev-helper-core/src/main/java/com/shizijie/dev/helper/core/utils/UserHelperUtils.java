package com.shizijie.dev.helper.core.utils;

import com.shizijie.dev.helper.core.mybatis.BaseMapperHelper;
import com.shizijie.dev.helper.core.plugins.HelperResult;
import com.shizijie.dev.helper.core.plugins.UserHelperBean;
import com.shizijie.dev.helper.core.plugins.UserHelperInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author shizijie
 * @version 2019-11-27 下午4:50
 */
public class UserHelperUtils {
    private final static String SYS_DATE="current_timestamp(6)";

    private final static String[] REMOVE_NAME={"DTO","VO","DO"};

    public static String getNewSql(String oldSql, String username, String type){
        Map<String,String> columnMap=new LinkedHashMap<>();
        setColumnsToMap(type,oldSql,UserHelperInterceptor.userHelperBean,columnMap,username);
        if(CollectionUtils.isEmpty(columnMap)){
            return oldSql;
        }
        if(UserHelperInterceptor.INSERT.equalsIgnoreCase(type)){
            return getNewInsertSql(oldSql,columnMap);
        }else if(UserHelperInterceptor.UPDATE.equalsIgnoreCase(type)){
            return getNewUpdateSql(oldSql,columnMap);
        }
        return oldSql;
    }

    private static String getNewUpdateSql(String oldSql, Map<String, String> columnMap) {
        String[] sqlArr=oldSql.replace("\n"," ").split(" ");
        StringBuffer newSql=new StringBuffer();
        for(String str:sqlArr){
            newSql.append(str);
            if("set".equalsIgnoreCase(str)){
                columnMap.forEach((k,v)->{
                    newSql.append(" "+k+" = "+v+",");
                });
            }
            newSql.append(" ");
        }
        return newSql.toString();
    }

    private static String getNewInsertSql(String oldSql, Map<String, String> columnMap) {
        List<String> column=new ArrayList<>(2);
        List<String> data=new ArrayList<>(2);
        columnMap.forEach((k,v)->{
            column.add(k);
            data.add(v);
        });
        String[] sqlArr=oldSql.replace("\n"," ").split(" ");
        StringBuffer newSql=new StringBuffer();
        int count=0;
        for(String str:sqlArr){
            if(str.indexOf("(")>=0){
                if(count==0){
                    str=str.replace("(","("+StringUtils.join(column," , ")+",");
                }else{
                    str="("+StringUtils.join(data," , ")+","+str.substring(1,str.length());
                }
                count++;
            }
            newSql.append(str);
            newSql.append(" ");
        }
        return newSql.toString();
    }

    private static boolean isBacthInsertSql(String sql){
        String subSql=sql.toLowerCase();
        subSql=subSql.substring(subSql.indexOf("values")).replace("\n","").replace(" ","");
        return subSql.indexOf("),(")>=0;
    }


    private static void setColumnsToMap(String sqlType,String sql,UserHelperBean userHelperBean,Map columnMap,String username){
        String subSql=null;
        if(UserHelperInterceptor.INSERT.equalsIgnoreCase(sqlType)){
            subSql=sql.toLowerCase().split("values")[0];
            if(subSql.indexOf(userHelperBean.getCreatedBy())==-1){
                columnMap.put(userHelperBean.getCreatedBy(),"'"+username+"'");
            }
            if(subSql.indexOf(userHelperBean.getCreatedDate())==-1){
                columnMap.put(userHelperBean.getCreatedDate(),SYS_DATE);
            }
        }else if(UserHelperInterceptor.UPDATE.equalsIgnoreCase(sqlType)){
            subSql=sql.toLowerCase().replace("\n"," ");
            if(subSql.indexOf("where")>=0){
                subSql=subSql.substring(subSql.indexOf(" set "),subSql.indexOf(" where "));
            }else{
                subSql=subSql.substring(subSql.indexOf(" set "));
            }
        }
        if(StringUtils.isNotBlank(subSql)){
            if(subSql.indexOf(userHelperBean.getUpdatedBy())==-1){
                columnMap.put(userHelperBean.getUpdatedBy(),"'"+username+"'");
            }
            if(subSql.indexOf(userHelperBean.getUpdatedDate())==-1){
                columnMap.put(userHelperBean.getUpdatedDate(),SYS_DATE);
            }
        }
    }

    public static HelperResult getSelectSqlByObject(Object object, Class<?> clazz) {
        HelperResult helperResult=new HelperResult();
        String tableName=getTableNameByClass(clazz);
        Field[] fields=clazz.getDeclaredFields();
        List<String> columnList=new ArrayList<>(fields.length);
        List<String> dataList=new ArrayList<>(fields.length);
        List<ParameterMapping> list=new ArrayList<>(fields.length);
        for(Field field:fields){
            String columnName=NameUtils.humpToLine(field.getName());
            columnList.add(columnName);
            if(ReflectUtils.getFieldValue(object,field.getName())!=null){
                dataList.add(columnName+" = ? ");
                list.add(INIT_PARAMETERMAPPING.apply(field.getName()));
            }
        }
        if(CollectionUtils.isEmpty(dataList)){
            helperResult.setNewSql(BaseMapperHelper.SELECT.replace("TABLE_NAME",tableName)
                    .replace("TABLE_COLUMNS",StringUtils.join(columnList,",")));
        }else{
            helperResult.setNewSql(BaseMapperHelper.SELECT.replace("TABLE_NAME",tableName)
                    .replace("TABLE_COLUMNS",StringUtils.join(columnList,","))
                    +" where "+StringUtils.join(dataList," and "));
            helperResult.setParameterMappings(list);
        }
        return helperResult;
    }

    public static HelperResult getInsertSqlByObject(Object object, Class<?> clazz) {
        HelperResult helperResult=new HelperResult();
        String tableName=getTableNameByClass(clazz);
        Field[] fields=clazz.getDeclaredFields();
        List<String> columnList=new ArrayList<>(fields.length);
        List<String> dataList=new ArrayList<>(fields.length);
        List<ParameterMapping> list=new ArrayList<>(fields.length);
        for(Field field:fields){
            String columnName=NameUtils.humpToLine(field.getName());
            if(UserHelperInterceptor.HELPER_COLUMNS.contains(columnName)){
                if(ReflectUtils.getFieldValue(object,field.getName())!=null){
                    columnList.add(columnName);
                    dataList.add("?");
                    list.add(INIT_PARAMETERMAPPING.apply(field.getName()));
                }
            }else{
                columnList.add(columnName);
                dataList.add("?");
                list.add(INIT_PARAMETERMAPPING.apply(field.getName()));
            }
        }
        helperResult.setNewSql(BaseMapperHelper.INSERT.replace("TABLE_NAME",tableName)
        .replace("TABLE_COLUMNS",StringUtils.join(columnList,","))
        .replace("TABLE_DATAS",StringUtils.join(dataList,",")));
        helperResult.setParameterMappings(list);
        return helperResult;
    }

    public static Function<String,ParameterMapping> INIT_PARAMETERMAPPING=(a)->{
        Configuration c=new Configuration();
        ParameterMapping.Builder builder=new ParameterMapping.Builder(c,a,Object.class);
        return builder.build();
    };

    public static HelperResult getDeleteSqlByObject(Object object, List<String> primary) {
        HelperResult helperResult=new HelperResult();
        String tableName=getTableNameByClass(object.getClass());
        if(!CollectionUtils.isEmpty(primary)){
            List<ParameterMapping> list=new ArrayList<>(primary.size());
            List<String> condition=new ArrayList<>(primary.size());
            for(String col:primary){
                condition.add(NameUtils.humpToLine(col)+" = ? ");
                list.add(INIT_PARAMETERMAPPING.apply("bean."+col));
            }
            helperResult.setParameterMappings(list);
            helperResult.setNewSql(BaseMapperHelper.DELETE.replace("TABLE_NAME",tableName)+" where "+StringUtils.join(condition," and "));
        }else{
            helperResult.setNewSql(BaseMapperHelper.DELETE.replace("TABLE_NAME",tableName));
        }
        return helperResult;
    }

    public static HelperResult getUpdateSqlByObject(Object object, List<String> primary) {
        HelperResult helperResult=new HelperResult();
        String tableName=getTableNameByClass(object.getClass());
        Field[] fields=object.getClass().getDeclaredFields();
        List<String> dataList=new ArrayList<>(fields.length);
        List<ParameterMapping> list=new ArrayList<>(fields.length);
        for(Field field:fields){
            String columnName=NameUtils.humpToLine(field.getName());
            if(ReflectUtils.getFieldValue(object,field.getName())!=null){
                dataList.add(columnName+" = ? ");
                list.add(INIT_PARAMETERMAPPING.apply("bean."+field.getName()));
            }
        }
        if(!CollectionUtils.isEmpty(primary)){
            List<String> condition=new ArrayList<>(primary.size());
            for(String col:primary){
                condition.add(col+" = ? ");
                list.add(INIT_PARAMETERMAPPING.apply("bean."+col));
            }
            helperResult.setNewSql(BaseMapperHelper.UPDATE.replace("TABLE_NAME",tableName)
                    .replace("TABLE_DATAS",StringUtils.join(dataList,","))+" where "+StringUtils.join(condition," and "));
        }else{
            helperResult.setNewSql(BaseMapperHelper.UPDATE.replace("TABLE_NAME",tableName)
                    .replace("TABLE_DATAS",StringUtils.join(dataList,",")));
        }
        helperResult.setParameterMappings(list);
        return helperResult;
    }

    private static String getTableNameByClass(Class clazz){
        String tableName=clazz.getName();
        tableName=tableName.substring(tableName.lastIndexOf(".")+1);
        for(String str:REMOVE_NAME){
            if(tableName.endsWith(str)){
                tableName=tableName.substring(0,tableName.length()-str.length());
            }
        }
        tableName=NameUtils.humpToLine(tableName);
        return tableName;
    }


}
