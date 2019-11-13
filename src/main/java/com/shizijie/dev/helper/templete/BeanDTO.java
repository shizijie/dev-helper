package com.shizijie.dev.helper.templete;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shizijie
 * @version 2019-11-12 下午5:20
 */
@Data
public class BeanDTO {
    private Integer num;
    private Date time;
    private BigDecimal sum;
}
