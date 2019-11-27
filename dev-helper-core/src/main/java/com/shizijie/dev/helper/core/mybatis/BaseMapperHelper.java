package com.shizijie.dev.helper.core.mybatis;

import org.apache.ibatis.annotations.Insert;

/**
 * @author shizijie
 * @version 2019-11-27 下午5:52
 */
public interface BaseMapperHelper<T> {
    String INSERT="insert into TABLE_NAME ( TABLE_COLUMNS ) values (TABLE_DATAS)";

    @Insert(INSERT)
    int insert(T t);
}
