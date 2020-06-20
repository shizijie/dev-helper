package com.shizijie.dev.helper.core.plugins;

import com.shizijie.dev.helper.core.annotation.UserHelper;
import com.shizijie.dev.helper.core.config.DevHelperConfiguration;
import com.shizijie.dev.helper.core.mybatis.BaseMapperHelper;
import com.shizijie.dev.helper.core.utils.ReflectUtils;
import com.shizijie.dev.helper.core.utils.UserHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author shizijie
 * @version 2019-11-27 下午3:59
 */
@Intercepts(value = {
        @Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class})
})
public class UserHelperInterceptor implements Interceptor {
    public final static UserHelperBean userHelperBean=new UserHelperBean();

    public final static List<String> HELPER_COLUMNS=new ArrayList<>(4);

    public final static String INSERT="insert";

    public final static String UPDATE="update";

    public final static String SELECT="select";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(invocation.getTarget() instanceof StatementHandler){
            try {
                RoutingStatementHandler handler= (RoutingStatementHandler) invocation.getTarget();
                StatementHandler delegate= (StatementHandler) ReflectUtils.getFieldValue(handler,"delegate");
                MappedStatement mappedStatement=(MappedStatement)ReflectUtils.getFieldValue(delegate,"mappedStatement");
                String type=mappedStatement.getSqlCommandType().name();
                BoundSql boundSql=delegate.getBoundSql();
                String oldSql=boundSql.getSql();
                Class<?> classType=Class.forName(mappedStatement.getId().substring(0,mappedStatement.getId().lastIndexOf(".")));
                if(INSERT.equalsIgnoreCase(type)||UPDATE.equalsIgnoreCase(type)){
                    String methodName=mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".")+1);

                    if(BaseMapperHelper.class.isAssignableFrom(classType)){
                        /** 增 */
                        if(BaseMapperHelper.INSERT.equals(oldSql)){
                            if(boundSql.getParameterObject()!=null){
                                HelperResult helperResult=UserHelperUtils.getInsertSqlByObject(boundSql.getParameterObject(),boundSql.getParameterObject().getClass());
                                String username= DevHelperConfiguration.userHelperApi.getUser(null);
                                String newSql= UserHelperUtils.getNewSql(helperResult.getNewSql(),username,type);
                                ReflectUtils.setFieldValue(boundSql,"parameterMappings",helperResult.getParameterMappings());
                                ReflectUtils.setFieldValue(boundSql,"sql",newSql);
                            }
                        }
                        /** 改 */
                        else if(BaseMapperHelper.UPDATE.equals(oldSql)){
                            if(boundSql.getParameterObject()!=null&&boundSql.getParameterObject() instanceof Map){
                                Map<String,Object> paramMap= (Map<String, Object>) boundSql.getParameterObject();
                                HelperResult helperResult=UserHelperUtils.getUpdateSqlByObject(paramMap.get("bean"),(List<String>)paramMap.get("list"));
                                String username= DevHelperConfiguration.userHelperApi.getUser(null);
                                String newSql= UserHelperUtils.getNewSql(helperResult.getNewSql(),username,type);
                                ReflectUtils.setFieldValue(boundSql,"parameterMappings",helperResult.getParameterMappings());
                                ReflectUtils.setFieldValue(boundSql,"sql",newSql);
                            }
                        }

                    }else{
                        /** userHelper 增/改 */
                        for(Method method:classType.getDeclaredMethods()){
                            if(method.isAnnotationPresent(UserHelper.class)&&method.getName().equals(methodName)){
                                UserHelper userHelper=method.getAnnotation(UserHelper.class);
                                String username= DevHelperConfiguration.userHelperApi.getUser(userHelper.value());
                                String newSql= UserHelperUtils.getNewSql(oldSql,username,type);
                                ReflectUtils.setFieldValue(boundSql,"sql",newSql);
                                break;
                            }
                        }
                    }
                }else if(BaseMapperHelper.class.isAssignableFrom(classType)){
                    /** 查 */
                    if(BaseMapperHelper.SELECT.equals(oldSql)){
                        HelperResult helperResult=UserHelperUtils.getSelectSqlByObject(boundSql.getParameterObject(),boundSql.getParameterObject().getClass());
                        ReflectUtils.setFieldValue(boundSql,"parameterMappings",helperResult.getParameterMappings());
                        ReflectUtils.setFieldValue(boundSql,"sql",helperResult.getNewSql());
                    }
                    /** 删 */
                    else if(BaseMapperHelper.DELETE.equals(oldSql)){
                        if(boundSql.getParameterObject()!=null&&boundSql.getParameterObject() instanceof Map){
                            Map<String,Object> paramMap= (Map<String, Object>) boundSql.getParameterObject();
                            HelperResult helperResult=UserHelperUtils.getDeleteSqlByObject(paramMap.get("bean"),(List<String>)paramMap.get("list"));
                            ReflectUtils.setFieldValue(boundSql,"parameterMappings",helperResult.getParameterMappings());
                            ReflectUtils.setFieldValue(boundSql,"sql",helperResult.getNewSql());
                        }
                    }
                }
                return invocation.proceed();
            } catch (Exception e) {
                throw new SQLException(e.getMessage(),e);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o,this);
    }

    @Override
    public void setProperties(Properties properties) {
        userHelperBean.setCreatedBy(properties.getProperty("createdBy"));
        userHelperBean.setCreatedDate(properties.getProperty("createdDate"));
        userHelperBean.setUpdatedBy(properties.getProperty("updatedBy"));
        userHelperBean.setUpdatedDate(properties.getProperty("updatedDate"));
        if(CollectionUtils.isEmpty(HELPER_COLUMNS)){
            HELPER_COLUMNS.add(properties.getProperty("createdBy"));
            HELPER_COLUMNS.add(properties.getProperty("createdDate"));
            HELPER_COLUMNS.add(properties.getProperty("updatedBy"));
            HELPER_COLUMNS.add(properties.getProperty("updatedDate"));
        }
    }
}
