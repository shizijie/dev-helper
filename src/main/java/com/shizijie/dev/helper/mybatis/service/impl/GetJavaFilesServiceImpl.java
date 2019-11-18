package com.shizijie.dev.helper.mybatis.service.impl;

import com.shizijie.dev.helper.mybatis.service.GetJavaFilesService;
import com.shizijie.dev.helper.mybatis.web.vo.GetJavaFilesVO;
import com.shizijie.dev.helper.utils.DataSourcesUtils;
import com.shizijie.dev.helper.utils.FileUtils;
import com.shizijie.dev.helper.utils.NameUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shizijie
 * @version 2019-11-12 下午5:23
 */
@Service
@Slf4j
//@SuppressWarnings("all")
public class GetJavaFilesServiceImpl implements GetJavaFilesService {
    @Override
    public String buildJavaByTableName(GetJavaFilesVO vo) {
        boolean hasDecimal=false;
        boolean hasDate=false;
        /** java实体 */
        StringBuffer entity=new StringBuffer();
        /** sql语句 */
        StringBuffer sql=new StringBuffer();
        try (Connection connection=DataSourcesUtils.getConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver())){
            if(connection==null){
                return "连接失败，请检查帐号密码等连接信息！";
            }
            DatabaseMetaData databaseMetaData=connection.getMetaData();
            ResultSet resultSet=databaseMetaData.getTables(null,"%","%",new String[]{"TABLE"});
            while (resultSet.next()){
                String name=resultSet.getString("TABLE_NAME");
                if(vo.getTableName().equals(name)){
                    ResultSet columns=databaseMetaData.getColumns(null,"%",vo.getTableName(),"%");
                    StringBuffer cols=new StringBuffer();
                    StringBuffer pros=new StringBuffer();
                    StringBuffer update=new StringBuffer();
                    while (columns.next()){
                        String columnName=columns.getString("COLUMN_NAME");
                        String columnType=columns.getString("TYPE_NAME");
                        String remark=columns.getString("REMARKS");
                        String privateName= DataSourcesUtils.underlineToCamel(columnName);
                        entity.append("\t/** "+remark+" */\n");
                        if("timestamp".equals(columnType.toLowerCase())){
                            entity.append("\tprivate Date "+privateName+";\n");
                            if(!hasDate){
                                hasDate=true;
                            }
                        }else if("numeric".equals(columnType.toLowerCase())){
                            entity.append("\tprivate BigDecimal "+privateName+";\n");
                            if(!hasDecimal){
                                hasDecimal=true;
                            }
                        }else{
                            entity.append("\tprivate String "+privateName+";\n");
                        }
                        cols.append(columnName+",");
                        pros.append("#{"+privateName+"},");
                        update.append(columnName+" = #{"+privateName+"},");
                    }
                    String colsArr=cols.toString().substring(0,cols.length()-1);
                    sql.append("-- insert\n");
                    sql.append("insert into "+name+" ("+colsArr+") values ("+pros.toString().substring(0,pros.length()-1)+");\n");
                    sql.append("\n-- select\n");
                    sql.append("select "+colsArr+" from "+name+";\n");
                    sql.append("\n-- update\n");
                    sql.append("update "+name+" set "+update.toString().substring(0,update.length()-1)+";");
                    break;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            return e.getMessage();
        }
        /** 删除文件 */
        FileUtils.deleteFileByPath(BASE_OUT_PATH+"/"+vo.getTableName());
        /** 生成sql文件 */
        FileUtils.createFile(BASE_OUT_PATH+"/"+vo.getTableName(),vo.getTableName()+SQL_FILE,sql.toString().getBytes());
        StringBuffer resultSb=new StringBuffer();
        for(String path:TEMPLATE_FILES){
            String result=null;
            if(JAVA_BEAN_TEMPLATE.equals(path)){
                result=buildFile(path,vo,entity.toString(),hasDecimal,hasDate);
            }else{
                result=buildFile(path,vo,null,null,null);
            }
            if(StringUtils.isNotBlank(result)){
                resultSb.append(result+"\n");
            }
        }
        if(StringUtils.isNotBlank(resultSb.toString())){
            return resultSb.toString();
        }
        return null;
    }

    public String buildFile(String templateFilePath, GetJavaFilesVO vo, String entityProperty, Boolean hasDecimal, Boolean hasDate){
        ClassPathResource resource=new ClassPathResource(templateFilePath);
        if(!resource.exists()){
            log.info("路径["+templateFilePath+"]下模板不存在!");
            return "路径["+templateFilePath+"]下模板不存在!";
        }
        try(InputStreamReader isr=new InputStreamReader(resource.getInputStream(),"utf-8");
            BufferedReader bf=new BufferedReader(isr)){
            String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String username=System.getProperty("user.name")==null?"userName":System.getProperty("user.name");
            String line=null;
            StringBuffer sb=new StringBuffer();
            while ((line=bf.readLine())!=null){
                line=line.replace(ENTITY_PACKAGE,vo.getPackageName()==null?"":vo.getPackageName());
                if(hasDecimal!=null){
                    if(hasDecimal){
                        line=line.replace(ENTITY_BIGDECIMAL,IMPORT_BIGDECIMAL);
                    }else{
                        line=line.replace(ENTITY_BIGDECIMAL,"");
                    }
                }
                if(hasDate!=null){
                    if(hasDate){
                        line=line.replace(ENTITY_DATE,IMPORT_DATE);
                    }else{
                        line=line.replace(ENTITY_DATE,"");
                    }
                }
                line=line.replace(ENTITY_CREATER,username);
                line=line.replace(ENTITY_CREATED_DATE,dateStr);
                line=line.replace(ENTITY_NAME,NameUtils.lineToHump(vo.getTableName()));
                if(StringUtils.isNotBlank(entityProperty)){
                    line=line.replace(ENTITY_PROPERTY,entityProperty);
                }
                sb.append(line);
                sb.append(ENTER);
            }
            String buildFileName=resource.getFilename().substring(0,resource.getFilename().lastIndexOf("."));
            buildFileName=buildFileName.replace("EntityTemplate",NameUtils.lineToHump(vo.getTableName()));
            if(templateFilePath.indexOf(TXT_FILE)!=-1){
                /** 生成java文件 */
                FileUtils.createFile(BASE_OUT_PATH+"/"+vo.getTableName(),buildFileName+JAVA_FILE,sb.toString().getBytes());
            }else{
                /** 生成xml文件 */
                FileUtils.createFile(BASE_OUT_PATH+"/"+vo.getTableName(),buildFileName+XML_FILE,sb.toString().getBytes());
            }
        }catch (IOException e) {
            log.error(e.getMessage(),e);
            return "["+templateFilePath+"]路径模板错误！"+e.getMessage();
        }
        return null;
    }
}
