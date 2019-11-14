package com.shizijie.dev.helper;

import com.shizijie.dev.helper.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author shizijie
 * @version 2019-11-14 11:23:44
 */
@RestController
@Slf4j
public class PfwTAuthorityController extends BaseController{
    @Autowired
    private PfwTAuthorityService PfwTAuthorityService;

}
