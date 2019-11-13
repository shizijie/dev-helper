package com.shizijie.dev.helper.mybatis.web;

import com.shizijie.dev.helper.common.BaseController;
import com.shizijie.dev.helper.mybatis.service.BuildJavaService;
import com.shizijie.dev.helper.mybatis.web.vo.GetTableColumnsVO;
import com.shizijie.dev.helper.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;

/**
 * @author shizijie
 * @version 2019-11-11 下午3:16
 */
@RestController
@Slf4j
public class MybatisController extends BaseController{
    @Autowired
    private BuildJavaService buildJavaService;
    @PostMapping("/getTableColumns")
    public void getsql(HttpServletResponse response,@Validated GetTableColumnsVO vo){
        String result=buildJavaService.buildJavaByTableName(vo);
        if(result==null){
            FileUtils.downloadBatchByFile(response, Paths.get(BuildJavaService.BASE_OUT_PATH+"/"+vo.getTableName()),vo.getTableName()+".zip");
        }else{
            log.error("读取错误["+result+"]");
        }
    }
}
