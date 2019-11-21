package com.shizijie.dev.helper.web.route;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shizijie
 * @version 2019-11-11 下午2:36
 */
@Controller
public class RouteController {
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/getJavaFiles")
    public String getJavaFiles(){
        return "getJavaFiles";
    }

    @RequestMapping("/createDatas")
    public String createDatas(){
        return "createDatas";
    }
}
