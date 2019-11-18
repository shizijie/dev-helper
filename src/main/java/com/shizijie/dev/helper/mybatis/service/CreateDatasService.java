package com.shizijie.dev.helper.mybatis.service;

import com.shizijie.dev.helper.mybatis.web.dto.ListTableByConnectionDTO;
import com.shizijie.dev.helper.mybatis.web.dto.QueryTableInfoDTO;
import com.shizijie.dev.helper.mybatis.web.vo.CheckConnectionVO;
import com.shizijie.dev.helper.mybatis.web.vo.GetDataSqlVO;
import com.shizijie.dev.helper.mybatis.web.vo.QueryTableInfoVO;

import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-15 上午10:44
 */
public interface CreateDatasService {
    String SQL="insert into TABLE_NAME (TABLE_COLUMNS_ARR) values (TABLE_DATAS)";

    String TABLE_NAME="TABLE_NAME";

    String TABLE_COLUMNS_ARR="TABLE_COLUMNS_ARR";

    String TABLE_DATAS="TABLE_DATAS";

    String SQL_OUT_PATH=System.getProperty("user.dir")+"/sqlfile";

    List<ListTableByConnectionDTO> listTableByConnection(CheckConnectionVO vo);

    List<QueryTableInfoDTO> queryTableInfo(QueryTableInfoVO vo);

    String getDataSql(GetDataSqlVO vo);
}
