package com.shizijie.dev.helper.mybatis.service;

import com.shizijie.dev.helper.mybatis.web.vo.GetJavaFilesVO;

/**
 * @author shizijie
 * @version 2019-11-12 下午5:23
 */
public interface GetJavaFilesService {
    String IMPORT_BIGDECIMAL="import java.math.BigDecimal;";

    String IMPORT_DATE="import java.util.Date;";

    //ClassPathResource ENTITY_TEMPLATE=new ClassPathResource("javatemplate/EntityTemplate.txt");

    String JAVA_BEAN_TEMPLATE="javatemplate/EntityTemplate.txt";

    String[] TEMPLATE_FILES={"javatemplate/EntityTemplate.txt",
            "javatemplate/EntityTemplateController.txt",
            "javatemplate/EntityTemplateService.txt",
            "javatemplate/EntityTemplateServiceImpl.txt",
            "javatemplate/EntityTemplateDao.txt",
            "javatemplate/EntityTemplateDao.xml"};

    String BASE_OUT_PATH=System.getProperty("user.dir")+"/buildfile";

    String JAVA_FILE=".java";

    String SQL_FILE=".sql";

    String TXT_FILE=".txt";

    String XML_FILE=".xml";

    String ENTER=System.getProperty("line.separator");

    /** ====替换信息==== */
    String ENTITY_PACKAGE="ENTITY_PACKAGE";

    String ENTITY_BIGDECIMAL="ENTITY_BIGDECIMAL";

    String ENTITY_DATE="ENTITY_DATE";

    String ENTITY_CREATER="ENTITY_CREATER";

    String ENTITY_CREATED_DATE="ENTITY_CREATED_DATE";

    String ENTITY_NAME="ENTITY_NAME";

    String ENTITY_PROPERTY="ENTITY_PROPERTY";

    String buildJavaByTableName(GetJavaFilesVO vo);
}
