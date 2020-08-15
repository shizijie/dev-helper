package com.shizijie.dev.helper.web.data.controller;

import com.shizijie.dev.helper.core.utils.FileUtils;
import com.shizijie.dev.helper.web.common.BaseController;
import com.shizijie.dev.helper.web.common.ResponseBean;
import com.shizijie.dev.helper.web.data.dto.DatabaseDTO;
import com.shizijie.dev.helper.web.data.service.DatasService;
import com.shizijie.dev.helper.web.data.service.JavaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;

import static com.shizijie.dev.helper.web.common.BuildFiles.JAVA_OUT_PATH;

@RestController
@RequestMapping("/java")
public class JavaController extends BaseController {
    @Autowired
    private JavaService javaService;
    @Autowired
    private DatasService datasService;

    @GetMapping("/getJavaFiles")
    public ResponseBean getJavaFiles(HttpServletResponse response, @RequestParam("id") String id,@RequestParam("tableName")  String tableName){
        DatabaseDTO databaseDTO=datasService.queryDatabaseById(id);
        if(databaseDTO==null){
            return fail("无有效服务器信息！");
        }
        String result=javaService.buildJavaByTableName(databaseDTO,tableName);
        if(result==null){
            FileUtils.downloadBatchByFile(response, Paths.get(JAVA_OUT_PATH+"/"+tableName),tableName+".zip");
            return null;
        }else{
            return fail("读取错误["+result+"]");
        }
    }
}
