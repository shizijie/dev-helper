package com.shizijie.dev.helper.mybatis.service;

import com.shizijie.dev.helper.mybatis.web.MybatisController;
import com.shizijie.dev.helper.mybatis.web.vo.GetTableColumnsVO;
import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;

/**
 * @author shizijie
 * @version 2019-11-12 下午5:23
 */
public interface BuildJavaService {
    String IMPORT_BIGDECIMAL="import java.math.BigDecimal;";

    String IMPORT_DATE="import java.util.Date;";

    ClassPathResource ENTITY_TEMPLATE=new ClassPathResource("javatemplate/EntityTemplate.txt");

    String BASE_OUT_PATH=System.getProperty("user.dir")+"/buildfile";

    /** ====替换信息==== */
    String ENTITY_PACKAGE="ENTITY_PACKAGE";

    String ENTITY_BIGDECIMAL="ENTITY_BIGDECIMAL";

    String ENTITY_DATE="ENTITY_DATE";

    String ENTITY_CREATER="ENTITY_CREATER";

    String ENTITY_CREATED_DATE="ENTITY_CREATED_DATE";

    String ENTITY_NAME="ENTITY_NAME";

    String ENTITY_PROPERTY="ENTITY_PROPERTY";

    String buildJavaByTableName(GetTableColumnsVO vo);
}
