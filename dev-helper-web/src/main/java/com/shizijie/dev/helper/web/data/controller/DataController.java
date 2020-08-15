package com.shizijie.dev.helper.web.data.controller;

import com.shizijie.dev.helper.core.utils.DataSourcesUtils;
import com.shizijie.dev.helper.web.common.BaseController;
import com.shizijie.dev.helper.web.common.ResponseBean;
import com.shizijie.dev.helper.web.data.service.DatasService;
import com.shizijie.dev.helper.web.data.dto.DatabaseDTO;
import com.shizijie.dev.helper.web.mybatis.web.vo.GetDataSqlVO;
import com.shizijie.dev.helper.web.mybatis.web.vo.QueryTableInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/data")
public class DataController extends BaseController {
    @Autowired
    private DatasService datasService;

    @GetMapping("/listDatabase")
    public ResponseBean listDatabase(){
        return success(datasService.listDatabase());
    }

    @PostMapping("/addDatabase")
    public ResponseBean addDatabase(@Validated @RequestBody DatabaseDTO dataBaseDTO){
        String result=DataSourcesUtils.checkConnection(dataBaseDTO.getUrl()+"/"+dataBaseDTO.getDatabase(),dataBaseDTO.getUsername(),dataBaseDTO.getPwd(),dataBaseDTO.getDriver());
        if(result==null){
            return noRespBack(datasService.addDatabase(dataBaseDTO));
        }
        return fail(result);
    }

    @GetMapping("/deleteDatabase")
    public ResponseBean deleteDatabase(@RequestParam("id") String id){
        datasService.deleteDatabase(id);
        return success();
    }

    @GetMapping("/checkDatabase")
    public ResponseBean checkDatabase(@RequestParam("id") String id){
        DatabaseDTO databaseDTO=datasService.queryDatabaseById(id);
        if(databaseDTO==null){
            return fail("无有效服务器信息！");
        }
        return noRespBack(DataSourcesUtils.checkConnection(databaseDTO.getUrl()+"/"+databaseDTO.getDatabase(),databaseDTO.getUsername(),databaseDTO.getPwd(),databaseDTO.getDriver()));
    }

    @GetMapping("/listTable")
    public ResponseBean listTable(@RequestParam("id") String id){
        DatabaseDTO databaseDTO=datasService.queryDatabaseById(id);
        return success(datasService.listTableByConnection(databaseDTO));
    }

    @PostMapping("/checkConnection")
    public ResponseBean checkConnection(@Validated @RequestBody DatabaseDTO vo){
        String result= DataSourcesUtils.checkConnection(vo.getUrl()+"/"+vo.getDatabase(),vo.getUsername(),vo.getPwd(),vo.getDriver());
        return noRespBack(result);
    }

    @PostMapping("/listTableByConnection")
    public ResponseBean listTableByConnection(@Validated @RequestBody DatabaseDTO vo){
        return success(datasService.listTableByConnection(vo));
    }

    @GetMapping("/queryTableInfo")
    public ResponseBean queryTableInfo( @RequestParam("id") String id,@RequestParam("tableName")  String tableName){
        DatabaseDTO databaseDTO=datasService.queryDatabaseById(id);
        if(databaseDTO==null){
            return fail("无有效服务器信息！");
        }
        return success(datasService.queryTableInfo(databaseDTO,tableName));
    }

    @PostMapping("/getDataSql")
    public ResponseBean getDataSql(@Validated @RequestBody GetDataSqlVO vo){
        return noRespBack(datasService.getDataSql(vo));
    }

}
