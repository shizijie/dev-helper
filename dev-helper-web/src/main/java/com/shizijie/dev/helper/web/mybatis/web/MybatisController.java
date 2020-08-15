package com.shizijie.dev.helper.web.mybatis.web;

import com.shizijie.dev.helper.web.mybatis.web.vo.GetJavaFilesVO;
import com.shizijie.dev.helper.web.common.BaseController;
import com.shizijie.dev.helper.web.common.DataEnum;
import com.shizijie.dev.helper.web.common.ResponseBean;
import com.shizijie.dev.helper.web.data.service.JavaService;
import com.shizijie.dev.helper.web.mybatis.web.dto.ListDataEnumDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2019-11-11 下午3:16
 */
@RestController
@Slf4j
public class MybatisController extends BaseController {
    @Autowired
    private JavaService getJavaFilesService;

    @PostMapping("/getJavaFiles")
    public void getJavaFiles(HttpServletResponse response,@Validated GetJavaFilesVO vo){
//        String result=getJavaFilesService.buildJavaByTableName(vo);
//        if(result==null){
//            FileUtils.downloadBatchByFile(response, Paths.get(JAVA_OUT_PATH+"/"+vo.getTableName()),vo.getTableName()+".zip");
//        }else{
//            log.error("读取错误["+result+"]");
//        }
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


//    @Autowired
//    private RedisMqHandler redisMqHandler;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @GetMapping("/test")
//    public void test(){
//        ListDataEnumDTO dto=new ListDataEnumDTO();
//        for(int i=1;i<=10;i++){
//            dto.setEnumName(String.valueOf(i));
//            redisTemplate.opsForList().leftPush("AAAA_QUEUE",i);
//            redisMqHandler.producer("AAAA",dto);
//        }
//        for(int i=1;i<=10;i++){
//            dto.setEnumName(String.valueOf(i));
//            redisMqHandler.producer("BBBB",dto);
//        }
//
//
////        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
////        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/test.lua")));
////        defaultRedisScript.setResultType(Long.class);
////        Object result=redisTemplate.execute(defaultRedisScript, Arrays.asList("xltop*"));
////        System.out.println(result);
//    }

}
