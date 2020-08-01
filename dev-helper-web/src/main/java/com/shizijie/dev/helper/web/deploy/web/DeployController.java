package com.shizijie.dev.helper.web.deploy.web;

import com.shizijie.dev.helper.web.common.BaseController;
import com.shizijie.dev.helper.web.common.ResponseBean;
import com.shizijie.dev.helper.web.deploy.dto.SSHDTO;
import com.shizijie.dev.helper.web.deploy.service.DeployService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shizijie
 * @version 2020-08-01 上午9:57
 */
@RestController
@Slf4j
public class DeployController extends BaseController {
    @Autowired
    private DeployService deployService;

    @GetMapping("/listSSH")
    public ResponseBean<List<SSHDTO>> listSSH(){
        return success(deployService.listSSH());
    }

    @PostMapping("/addService")
    public ResponseBean addService(@Validated @RequestBody SSHDTO sshdto){
        return noRespBack(deployService.addService(sshdto));
    }
}
