package com.shizijie.dev.helper.mybatis.service;

import com.shizijie.dev.helper.mybatis.web.dto.ListTableByConnectionDTO;
import com.shizijie.dev.helper.mybatis.web.vo.CheckConnectionVO;

import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-15 上午10:44
 */
public interface CreateDatasService {
    List<ListTableByConnectionDTO> listTableByConnection(CheckConnectionVO vo);
}
