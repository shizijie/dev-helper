package com.shizijie.dev.helper.web.mybatis.web;

import com.shizijie.dev.helper.core.api.user.RedisMqHandler;
import com.shizijie.dev.helper.core.utils.DataSourcesUtils;
import com.shizijie.dev.helper.core.utils.FileUtils;
import com.shizijie.dev.helper.web.mybatis.web.vo.GetJavaFilesVO;
import com.shizijie.dev.helper.web.common.BaseController;
import com.shizijie.dev.helper.web.common.DataEnum;
import com.shizijie.dev.helper.web.common.ResponseBean;
import com.shizijie.dev.helper.web.mybatis.service.CreateDatasService;
import com.shizijie.dev.helper.web.mybatis.service.GetJavaFilesService;
import com.shizijie.dev.helper.web.mybatis.web.dto.ListDataEnumDTO;
import com.shizijie.dev.helper.web.mybatis.web.vo.CheckConnectionVO;
import com.shizijie.dev.helper.web.mybatis.web.vo.GetDataSqlVO;
import com.shizijie.dev.helper.web.mybatis.web.vo.QueryTableInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-11 下午3:16
 */
@RestController
@Slf4j
public class MybatisController extends BaseController {
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
        String result= DataSourcesUtils.checkConnection(vo.getUrl(),vo.getUsername(),vo.getPwd(),vo.getDriver());
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

    @PostMapping("/getDataSql")
    public ResponseBean getDataSql(@Validated @RequestBody GetDataSqlVO vo){
        return back(createDatasService.getDataSql(vo));
    }


    @Autowired
    private RedisMqHandler redisMqHandler;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public void test(){
        ListDataEnumDTO dto=new ListDataEnumDTO();
        for(int i=1;i<=10;i++){
            dto.setEnumName(String.valueOf(i));
            redisTemplate.opsForList().leftPush("AAAA_QUEUE",i);
            redisMqHandler.producer("AAAA",dto);
        }
        for(int i=1;i<=10;i++){
            dto.setEnumName(String.valueOf(i));
            redisMqHandler.producer("BBBB",dto);
        }


//        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
//        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/test.lua")));
//        defaultRedisScript.setResultType(Long.class);
//        Object result=redisTemplate.execute(defaultRedisScript, Arrays.asList("xltop*"));
//        System.out.println(result);
    }

}
