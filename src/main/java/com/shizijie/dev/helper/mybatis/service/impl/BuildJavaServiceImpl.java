package com.shizijie.dev.helper.mybatis.service.impl;

import com.shizijie.dev.helper.DevHelperApplication;
import com.shizijie.dev.helper.mybatis.service.BuildJavaService;
import com.shizijie.dev.helper.mybatis.web.vo.GetTableColumnsVO;
import com.shizijie.dev.helper.utils.DataSourcesUtils;
import com.shizijie.dev.helper.utils.FileUtils;
import com.shizijie.dev.helper.utils.NameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shizijie
 * @version 2019-11-12 下午5:23
 */
@Service
@Slf4j
//@SuppressWarnings("all")
public class BuildJavaServiceImpl implements BuildJavaService {
    @Override
    public String buildJavaByTableName(GetTableColumnsVO vo) {
        boolean hasDecimal=false;
        boolean hasDate=false;
        StringBuffer entity=new StringBuffer();
        try (Connection connection=DataSourcesUtils.getConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver())){
            DatabaseMetaData databaseMetaData=connection.getMetaData();
            ResultSet resultSet=databaseMetaData.getTables(null,"%","%",new String[]{"TABLE"});
            while (resultSet.next()){
                String name=resultSet.getString("TABLE_NAME");
                if(vo.getTableName().equals(name)){
                    ResultSet columns=databaseMetaData.getColumns(null,"%",vo.getTableName(),"%");
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
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            return e.getMessage();
        }
        if(!ENTITY_TEMPLATE.exists()){
            log.info("路径[javatemplate]下模板[EntityTemplate.txt]不存在");
            return "不存在EntityTemplate.txt模板！";
        }
        try {
            Path outPath=Paths.get(BASE_OUT_PATH+"/"+vo.getTableName());
            if(Files.exists(outPath)){
                FileUtils.deleteFile(new File(BASE_OUT_PATH+"/"+vo.getTableName()));
            }
            new File(outPath.toUri()).mkdirs();
            String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String username=System.getenv().get("USERNAME")==null?"userName":System.getenv().get("USERNAME");
            String line=null;
            StringBuffer sb=new StringBuffer();
            try(InputStreamReader isr=new InputStreamReader(ENTITY_TEMPLATE.getInputStream(),"utf-8");
                    BufferedReader bf=new BufferedReader(isr)){
                while ((line=bf.readLine())!=null){
                    line=line.replace(ENTITY_PACKAGE,"packagename");
                    if(hasDecimal){
                        line=line.replace(ENTITY_BIGDECIMAL,IMPORT_BIGDECIMAL);
                    }else{
                        line=line.replace(ENTITY_BIGDECIMAL,"");
                    }
                    if(hasDate){
                        line=line.replace(ENTITY_DATE,IMPORT_DATE);
                    }else{
                        line=line.replace(ENTITY_DATE,"");
                    }
                    line=line.replace(ENTITY_CREATER,username);
                    line=line.replace(ENTITY_CREATED_DATE,dateStr);
                    line=line.replace(ENTITY_NAME,NameUtils.lineToHump(vo.getTableName()));
                    line=line.replace(ENTITY_PROPERTY,entity.toString());
                    sb.append(line);
                    sb.append(System.getProperty("line.separator"));
                }
            }
            Path out=Paths.get(BASE_OUT_PATH+"/"+vo.getTableName(), NameUtils.lineToHump(vo.getTableName())+".java");
            if(!Files.exists(out)){
                Files.createFile(out);
            }
            Files.write(out,sb.toString().getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            return e.getMessage();
        }
        return null;
    }



}
