package com.shizijie.dev.helper.mybatis.service.impl;

import com.shizijie.dev.helper.common.DataEnum;
import com.shizijie.dev.helper.mybatis.service.CreateDatasService;
import com.shizijie.dev.helper.mybatis.service.GetJavaFilesService;
import com.shizijie.dev.helper.mybatis.web.dto.ListTableByConnectionDTO;
import com.shizijie.dev.helper.mybatis.web.dto.QueryTableInfoDTO;
import com.shizijie.dev.helper.mybatis.web.vo.CheckConnectionVO;
import com.shizijie.dev.helper.mybatis.web.vo.GetDataSqlVO;
import com.shizijie.dev.helper.mybatis.web.vo.QueryTableInfoVO;
import com.shizijie.dev.helper.utils.DataSourcesUtils;
import com.shizijie.dev.helper.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.*;

import static com.shizijie.dev.helper.mybatis.service.GetJavaFilesService.SQL_FILE;

/**
 * @author shizijie
 * @version 2019-11-15 上午10:44
 */
@Service
@Slf4j
public class CreateDatasServiceImpl implements CreateDatasService {
    @Override
    public List<ListTableByConnectionDTO> listTableByConnection(CheckConnectionVO vo) {
        try (Connection connection= DataSourcesUtils.getConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver())){
            if(connection==null){
                return Collections.EMPTY_LIST;
            }
            DatabaseMetaData databaseMetaData=connection.getMetaData();
            ResultSet resultSet=databaseMetaData.getTables(null,"%","%",new String[]{"TABLE"});
            List<ListTableByConnectionDTO> list=new ArrayList<>();
            ListTableByConnectionDTO dto=null;
            List<String> colums=null;
            while (resultSet.next()){
                String name=resultSet.getString("TABLE_NAME");
                dto=new ListTableByConnectionDTO();
                dto.setTableName(name);
                ResultSet columns=databaseMetaData.getColumns(null,"%",name,"%");
                colums=new ArrayList<>();
                while (columns.next()){
                    String columnName=columns.getString("COLUMN_NAME");
                    colums.add(columnName);
                }
                dto.setColumnsName(StringUtils.join(colums," , "));
                list.add(dto);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<QueryTableInfoDTO> queryTableInfo(QueryTableInfoVO vo) {
        try (Connection connection= DataSourcesUtils.getConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver())){
            if(connection==null){
                return Collections.EMPTY_LIST;
            }
            DatabaseMetaData databaseMetaData=connection.getMetaData();
            ResultSet resultSet=databaseMetaData.getTables(null,"%","%",new String[]{"TABLE"});
            while (resultSet.next()){
                String name=resultSet.getString("TABLE_NAME");
                if(name.equals(vo.getTableName())){
                    List<QueryTableInfoDTO> list=new ArrayList<>();
                    QueryTableInfoDTO dto=null;
                    ResultSet columns=databaseMetaData.getColumns(null,"%",name,"%");
                    while (columns.next()){
                        dto=new QueryTableInfoDTO();
                        String columnName=columns.getString("COLUMN_NAME");
                        String columnType=columns.getString("TYPE_NAME");
                        String remark=columns.getString("REMARKS");
                        dto.setColumnName(columnName);
                        dto.setColumnType(columnType);
                        dto.setColumnRemark(remark);
                        list.add(dto);
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getDataSql(GetDataSqlVO vo) {
        PreparedStatement ps = null;
        ResultSet result = null;
        List<String> listColumns=null;
        try (Connection connection= DataSourcesUtils.getConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver())){
            if(connection==null){
                return "连接失败，请检查帐号密码等连接信息！";
            }
            for(int i=0;i<vo.getColumnName().size();i++){
                if(DataEnum.SQL_VALUE.getCode().equals(vo.getDictType().get(i))){
                    ps = connection.prepareStatement(vo.getDictValue().get(i));
                    result = ps.executeQuery();
                    if(listColumns==null){
                        listColumns=new ArrayList<>(10001);
                    }else{
                        return "目前仅支持最多一个sql造数！";
                    }
                    while (result.next()) {
                        if(listColumns.size()>10000){
                            throw new SQLException("["+vo.getColumnName().get(i)+"]查询结果超过1W！");
                        }
                        listColumns.add(result.getString(1));
                    }
                }
            }
            StringBuffer sb=new StringBuffer();
            while (vo.getNumber()>0){
                if(CollectionUtils.isEmpty(listColumns)){
                    listColumns=new ArrayList<>(1);
                    listColumns.add("null");
                }
                for(String sqlColumn:listColumns){
                    String tmpsql=SQL.replace(TABLE_NAME,vo.getTableName());
                    List<String> columns=new ArrayList<>(vo.getColumnName().size());
                    List<String> datas=new ArrayList<>(vo.getColumnName().size());
                    for(int i=0;i<vo.getColumnName().size();i++){
                        columns.add(vo.getColumnName().get(i));
                        switch (DataEnum.getByCode(vo.getDictType().get(i))){
                            //uuid
                            case UUID:{
                                datas.add("'"+UUID.randomUUID().toString().replace("-","").toLowerCase()+"'");
                                break;
                            }
                            //随机数
                            case RONDOM_NUM:{
                                if(StringUtils.isBlank(vo.getDictValue().get(i))){
                                    datas.add(new Random().nextInt((int)Math.pow(10,2))+"");
                                }else if(vo.getDictValue().get(i).indexOf(",")!=-1){
                                    String[] str=vo.getDictValue().get(i).split(",");
                                    Integer start=2;
                                    Integer end=2;
                                    try {
                                        start=Integer.parseInt(str[0]);
                                    } catch (Exception e) {
                                        start=2;
                                    }
                                    try {
                                        end=Integer.parseInt(str[1]);
                                    } catch (Exception e) {
                                        end=2;
                                    }
                                    datas.add(new Random().nextInt((int)Math.pow(10,start))+"."+new Random().nextInt((int)Math.pow(10,end)));
                                }else{
                                    Integer num=2;
                                    try {
                                        num=Integer.parseInt(vo.getDictValue().get(i));
                                    } catch (Exception e) {
                                        num=2;
                                    }
                                    datas.add(new Random().nextInt((int)Math.pow(10,num))+"");
                                }
                                break;
                            }
                            //固定值
                            case FIXED_VALUE:{
                                if(StringUtils.isNotBlank(vo.getDictValue().get(i))){
                                    datas.add("'"+vo.getDictValue().get(i)+"'");
                                }else{
                                    datas.add("null");
                                }
                                break;
                            }
                            //db字段值
                            case SQL_VALUE:{
                                datas.add("'"+sqlColumn+"'");
                                break;
                            }
                            case DB_FUNCTION:
                                datas.add(vo.getDictValue().get(i)+"");
                                break;
                        }
                    }
                    sb.append(tmpsql.replace(TABLE_COLUMNS_ARR,StringUtils.join(columns,","))
                            .replace(TABLE_DATAS,StringUtils.join(datas,",")));
                    sb.append(GetJavaFilesService.ENTER);
                }
                vo.setNumber(vo.getNumber()-1);
            }
            FileUtils.deleteFileByPath(SQL_OUT_PATH+"/"+vo.getTableName());
            FileUtils.createFile(SQL_OUT_PATH+"/"+vo.getTableName(),vo.getTableName()+SQL_FILE,sb.toString().getBytes());
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return e.getMessage();
        }
        return null;
    }
}
