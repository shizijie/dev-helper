package com.shizijie.dev.helper.mybatis.web;

import com.shizijie.dev.helper.common.BaseController;
import com.shizijie.dev.helper.common.DataEnum;
import com.shizijie.dev.helper.common.ResponseBean;
import com.shizijie.dev.helper.mybatis.service.CreateDatasService;
import com.shizijie.dev.helper.mybatis.service.GetJavaFilesService;
import com.shizijie.dev.helper.mybatis.web.dto.ListDataEnumDTO;
import com.shizijie.dev.helper.mybatis.web.vo.CheckConnectionVO;
import com.shizijie.dev.helper.mybatis.web.vo.GetJavaFilesVO;
import com.shizijie.dev.helper.mybatis.web.vo.QueryTableInfoVO;
import com.shizijie.dev.helper.utils.DataSourcesUtils;
import com.shizijie.dev.helper.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shizijie
 * @version 2019-11-11 下午3:16
 */
@RestController
@Slf4j
public class MybatisController extends BaseController{
    @Autowired
    private GetJavaFilesService getJavaFilesService;
    @Autowired
    private CreateDatasService createDatasService;

    @PostMapping("/getJavaFiles")
    public void getJavaFiles(HttpServletResponse response,@Validated GetJavaFilesVO vo){
        String result=getJavaFilesService.buildJavaByTableName(vo);
        if(result==null){
            FileUtils.downloadBatchByFile(response, Paths.get(GetJavaFilesService.BASE_OUT_PATH+"/"+vo.getTableName()),vo.getTableName()+".zip");
        }else{
            log.error("读取错误["+result+"]");
        }
    }
    @PostMapping("/checkConnection")
    public ResponseBean checkConnection(@Validated @RequestBody CheckConnectionVO vo){
        String result=DataSourcesUtils.checkConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver());
        return back(result);
    }

    @PostMapping("/listTableByConnection")
    public ResponseBean listTableByConnection(@Validated @RequestBody CheckConnectionVO vo){
        return success(createDatasService.listTableByConnection(vo));
    }

    @PostMapping("/queryTableInfo")
    public ResponseBean queryTableInfo(@Validated @RequestBody QueryTableInfoVO vo){
        return success(createDatasService.queryTableInfo(vo));
    }

    @GetMapping("/listDataEnum")
    public ResponseBean listDataEnum(){
        List<ListDataEnumDTO> list=new ArrayList<>();
        ListDataEnumDTO dto=null;
        for(DataEnum dataEnum:DataEnum.values()){
            dto=new ListDataEnumDTO();
            dto.setEnumCode(dataEnum.getCode());
            dto.setEnumName(dataEnum.getName());
            list.add(dto);
        }
        return success(list);
    }

}
