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

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author shizijie
 * @version 2019-11-27 下午3:59
 */
@Intercepts(value = {
        @Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class})
})
@Slf4j
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
                MappedStatement mappedStatement=(MappedStatement)ReflectUtils.getFieldValue(handler,"mappedStatement");
                String type=mappedStatement.getSqlCommandType().name();
                if(INSERT.equalsIgnoreCase(type)||UPDATE.equalsIgnoreCase(type)){
                    BoundSql boundSql=delegate.getBoundSql();
                    Class<?> classType=Class.forName(mappedStatement.getId().substring(0,mappedStatement.getId().lastIndexOf(".")));
                    String methodName=mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".")+1);

                    if(BaseMapperHelper.class.isAssignableFrom(classType)&&"insert".equalsIgnoreCase(type)){
                        HelperResult helperResult=UserHelperUtils.getInsertSqlByObject(boundSql.getParameterObject(),boundSql.getParameterObject().getClass());
                        String username= DevHelperConfiguration.userHelperApi.getUser("");
                        String newSql= UserHelperUtils.getNewSql(helperResult.getNewSql(),username,type);
                        ReflectUtils.setFieldValue(boundSql,"parameterMappings",helperResult.getParameterMappings());
                        ReflectUtils.setFieldValue(boundSql,"sql",newSql);
                        return invocation.proceed();
                    }
                    for(Method method:classType.getDeclaredMethods()){
                        if(method.isAnnotationPresent(UserHelper.class)&&method.getName().equals(methodName)){
                            String oldSql=boundSql.getSql();
                            UserHelper userHelper=method.getAnnotation(UserHelper.class);
                            String username= DevHelperConfiguration.userHelperApi.getUser(userHelper.value());
                            String newSql= UserHelperUtils.getNewSql(oldSql,username,type);
                            ReflectUtils.setFieldValue(boundSql,"sql",newSql);
                            return invocation.proceed();
                        }
                    }
                }else if(SELECT.equalsIgnoreCase(type)){

                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
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
        HELPER_COLUMNS.add(properties.getProperty("createdBy"));
        HELPER_COLUMNS.add(properties.getProperty("createdDate"));
        HELPER_COLUMNS.add(properties.getProperty("updatedBy"));
        HELPER_COLUMNS.add(properties.getProperty("updatedDate"));

    }
}
