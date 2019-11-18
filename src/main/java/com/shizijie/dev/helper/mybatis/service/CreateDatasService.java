package com.shizijie.dev.helper.mybatis.service;

import com.shizijie.dev.helper.mybatis.web.dto.ListTableByConnectionDTO;
import com.shizijie.dev.helper.mybatis.web.dto.QueryTableInfoDTO;
import com.shizijie.dev.helper.mybatis.web.vo.CheckConnectionVO;
import com.shizijie.dev.helper.mybatis.web.vo.QueryTableInfoVO;

import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-15 上午10:44
 */
public interface CreateDatasService {
    List<ListTableByConnectionDTO> listTableByConnection(CheckConnectionVO vo);

    List<QueryTableInfoDTO> queryTableInfo(QueryTableInfoVO vo);
}
