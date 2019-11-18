package com.shizijie.dev.helper.mybatis.service.impl;

import com.shizijie.dev.helper.mybatis.service.CreateDatasService;
import com.shizijie.dev.helper.mybatis.web.dto.ListTableByConnectionDTO;
import com.shizijie.dev.helper.mybatis.web.dto.QueryTableInfoDTO;
import com.shizijie.dev.helper.mybatis.web.vo.CheckConnectionVO;
import com.shizijie.dev.helper.mybatis.web.vo.QueryTableInfoVO;
import com.shizijie.dev.helper.utils.DataSourcesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
