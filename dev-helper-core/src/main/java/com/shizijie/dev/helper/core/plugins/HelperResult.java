package com.shizijie.dev.helper.core.plugins;

import lombok.Data;
import org.apache.ibatis.mapping.ParameterMapping;

import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-27 下午5:57
 */
@Data
public class HelperResult {
    private String newSql;
    private List<ParameterMapping> parameterMappings;
}
