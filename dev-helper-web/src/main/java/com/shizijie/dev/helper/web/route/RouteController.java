package com.shizijie.dev.helper.web.route;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shizijie
 * @version 2019-11-11 下午2:36
 */
@Controller
public class RouteController {
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/getJavaFiles")
    public String getJavaFiles(){
        return "getJavaFiles";
    }

    @GetMapping("/createDatas")
    public String createDatas(){
        return "createDatas";
    }


}
