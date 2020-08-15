package com.shizijie.dev.helper.web.data.service.impl;

import com.shizijie.dev.helper.core.utils.DataSourcesUtils;
import com.shizijie.dev.helper.core.utils.FileUtils;
import com.shizijie.dev.helper.web.common.DataEnum;
import com.shizijie.dev.helper.web.common.utils.GsonUtils;
import com.shizijie.dev.helper.web.data.service.JavaService;
import com.shizijie.dev.helper.web.mybatis.web.dto.ListTableByConnectionDTO;
import com.shizijie.dev.helper.web.mybatis.web.dto.QueryTableInfoDTO;
import com.shizijie.dev.helper.web.data.dto.DatabaseDTO;
import com.shizijie.dev.helper.web.mybatis.web.vo.GetDataSqlVO;
import com.shizijie.dev.helper.web.mybatis.web.vo.QueryTableInfoVO;
import com.shizijie.dev.helper.web.data.service.DatasService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

import static com.shizijie.dev.helper.web.common.BuildFiles.DATABASE_PATH;
import static com.shizijie.dev.helper.web.common.BuildFiles.SQL_OUT_PATH;
import static com.shizijie.dev.helper.web.data.service.JavaService.SQL_FILE;


/**
 * @author shizijie
 * @version 2019-11-15 上午10:44
 */
@Service
@Slf4j
public class DatasServiceImpl implements DatasService {
    @Override
    public List<ListTableByConnectionDTO> listTableByConnection(DatabaseDTO vo) {
        try (Connection connection= DataSourcesUtils.getConnection(vo.getUrl()+"/"+vo.getDatabase(),vo.getUsername(),vo.getPwd(),vo.getDriver())){
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
    public List<QueryTableInfoDTO> queryTableInfo(DatabaseDTO databaseDTO, String tableName) {
        try (Connection connection= DataSourcesUtils.getConnection(databaseDTO.getUrl()+"/"+databaseDTO.getDatabase(),databaseDTO.getUsername(),databaseDTO.getPwd(),databaseDTO.getDriver())){
            if(connection==null){
                return Collections.EMPTY_LIST;
            }
            DatabaseMetaData databaseMetaData=connection.getMetaData();
            ResultSet resultSet=databaseMetaData.getTables(null,"%","%",new String[]{"TABLE"});
            while (resultSet.next()){
                String name=resultSet.getString("TABLE_NAME");
                if(name.equals(tableName)){
                    List<QueryTableInfoDTO> list=new ArrayList<>();
                    QueryTableInfoDTO dto=null;
                    ResultSet columns=databaseMetaData.getColumns(null,"%",name,"%");
                    while (columns.next()){
                        dto=new QueryTableInfoDTO();
                        String columnName=columns.getString("COLUMN_NAME");
                        String columnType=columns.getString("TYPE_NAME").toLowerCase();
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
        List<String> listColumns=null;
        try (Connection connection= DataSourcesUtils.getConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver())){
            if(connection==null){
                return "连接失败，请检查帐号密码等连接信息！";
            }
            for(int i=0;i<vo.getColumnName().size();i++){
                if(DataEnum.SQL_VALUE.getCode().equals(vo.getDictType().get(i))){
                    if(listColumns==null){
                        listColumns=DataEnum.getDataByCode(vo.getDictType().get(i),vo.getDictValue().get(i),connection);
                    }else{
                        return "目前仅支持最多一个sql造数！";
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
                        if(DataEnum.SQL_VALUE.getCode().equals(vo.getDictType().get(i))){
                            datas.add("'"+sqlColumn+"'");
                        }else{
                            datas.add(DataEnum.getDataByCode(vo.getDictType().get(i),vo.getDictValue().get(i),null).get(0));
                        }
                    }
                    sb.append(tmpsql.replace(TABLE_COLUMNS_ARR,StringUtils.join(columns,","))
                            .replace(TABLE_DATAS,StringUtils.join(datas,",")));
                    sb.append(JavaService.ENTER);
                }
                vo.setNumber(vo.getNumber()-1);
            }
            FileUtils.deleteFileByPath(SQL_OUT_PATH+"/"+vo.getTableName());
            FileUtils.createFile(SQL_OUT_PATH+"/"+vo.getTableName(),vo.getTableName()+ SQL_FILE,sb.toString().getBytes());
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return e.getMessage();
        }
        return null;
    }

    @Override
    public List<DatabaseDTO> listDatabase() {
        return FileUtils.readFileContent(DATABASE_PATH+"/"+DATA_BASE, DatabaseDTO.class);
    }

    @Override
    public String addDatabase(DatabaseDTO dataBaseDTO) {
        List<DatabaseDTO> list=listDatabase();
        if(!CollectionUtils.isEmpty(list)){
            for(DatabaseDTO data:list){
                if(StringUtils.isNoneBlank(dataBaseDTO.getId())){
                    BeanUtils.copyProperties(dataBaseDTO,data);
                    break;
                }else{
                    String url=data.getUrl()+"/"+data.getDatabase();
                    if(url.equals(dataBaseDTO.getUrl()+"/"+dataBaseDTO.getDatabase())){
                        return "url : "+url+"已存在！";
                    }
                }
            }
        }else{
            list=new ArrayList<>();
        }
        if(StringUtils.isBlank(dataBaseDTO.getId())){
            dataBaseDTO.setId(UUID.randomUUID().toString());
            list.add(dataBaseDTO);
        }
        FileUtils.createFile(DATABASE_PATH,DATA_BASE, GsonUtils.GSON.toJson(list).getBytes());
        return null;
    }

    @Override
    public void deleteDatabase(String id) {
        List<DatabaseDTO> list=listDatabase();
        if(!CollectionUtils.isEmpty(list)){
            Iterator<DatabaseDTO> it= list.iterator();
            while (it.hasNext()){
                DatabaseDTO dto=it.next();
                if(dto.getId().equals(id)){
                    it.remove();
                    break;
                }
            }
            FileUtils.createFile(DATABASE_PATH,DATA_BASE, GsonUtils.GSON.toJson(list).getBytes());
        }
    }

    @Override
    public DatabaseDTO queryDatabaseById(String id) {
        List<DatabaseDTO> list=listDatabase();
        if(!CollectionUtils.isEmpty(list)){
            for(DatabaseDTO dataBaseDTO:list){
                if(dataBaseDTO.getId().equals(id)){
                    return dataBaseDTO;
                }
            }
        }
        return null;
    }
}
