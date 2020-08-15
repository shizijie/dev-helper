package com.shizijie.dev.helper.web.data.service;


import com.shizijie.dev.helper.web.mybatis.web.dto.ListTableByConnectionDTO;
import com.shizijie.dev.helper.web.mybatis.web.dto.QueryTableInfoDTO;
import com.shizijie.dev.helper.web.data.dto.DatabaseDTO;
import com.shizijie.dev.helper.web.mybatis.web.vo.GetDataSqlVO;
import com.shizijie.dev.helper.web.mybatis.web.vo.QueryTableInfoVO;

import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-15 上午10:44
 */
public interface DatasService {
    String SQL="insert into TABLE_NAME (TABLE_COLUMNS_ARR) values (TABLE_DATAS);";

    String TABLE_NAME="TABLE_NAME";

    String TABLE_COLUMNS_ARR="TABLE_COLUMNS_ARR";

    String TABLE_DATAS="TABLE_DATAS";

    String DATA_BASE="database.txt";

    List<ListTableByConnectionDTO> listTableByConnection(DatabaseDTO vo);

    List<QueryTableInfoDTO> queryTableInfo(DatabaseDTO databaseDTO, String tableName);

    String getDataSql(GetDataSqlVO vo);

    List<DatabaseDTO> listDatabase();

    String addDatabase(DatabaseDTO dataBaseDTO);

    void deleteDatabase(String id);

    DatabaseDTO queryDatabaseById(String id);

}
