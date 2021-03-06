package com.shizijie.dev.helper.core.mybatis;

import org.apache.ibatis.annotations.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-27 下午5:52
 */
@Validated
public interface BaseMapperHelper<T> {
    String INSERT="insert into TABLE_NAME ( TABLE_COLUMNS ) values (TABLE_DATAS)";

    String SELECT="select TABLE_COLUMNS from TABLE_NAME";

    String UPDATE="update TABLE_NAME set TABLE_DATAS";

    String DELETE="delete from TABLE_NAME";

    @Insert(INSERT)
    int insert(@NotNull T t);

    @Select(SELECT)
    List<T> list(@NotNull T t);

    @Select(SELECT)
    T query(@NotNull T t);

    @Update(UPDATE)
    int update(@NotNull @Param("bean") T t,@Param("list")List<String> list);

    @Delete(DELETE)
    int delete(@NotNull @Param("bean") T t,@Param("list")List<String> list);
}
