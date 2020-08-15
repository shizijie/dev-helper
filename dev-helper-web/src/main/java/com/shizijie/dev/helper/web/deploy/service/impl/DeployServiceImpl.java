package com.shizijie.dev.helper.web.deploy.service.impl;

import ch.ethz.ssh2.Connection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shizijie.dev.helper.core.utils.FileUtils;
import com.shizijie.dev.helper.core.utils.SSHUtils;
import com.shizijie.dev.helper.web.deploy.dto.SSHDTO;
import com.shizijie.dev.helper.web.deploy.service.DeployService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shizijie.dev.helper.web.common.BuildFiles.DEPLOY_PATH;

/**
 * @author shizijie
 * @version 2020-08-01 上午10:06
 */
@Service
public class DeployServiceImpl implements DeployService {
    private Gson gson=new Gson();

    @Override
    public List<SSHDTO> listSSH() {
        return FileUtils.readFileContent(DEPLOY_PATH+"/"+SSH,SSHDTO.class);
    }

    @Override
    public String addService(SSHDTO sshdto) {
        Connection con= null;
        try {
            con = SSHUtils.login(sshdto.getIp(),sshdto.getUsername(),sshdto.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        if(con==null){
            return "连接失败！";
        }
        List<SSHDTO> list=listSSH();
        if(!CollectionUtils.isEmpty(list)){
            for(SSHDTO ssh:list){
                if(ssh.getIp().equals(sshdto.getIp())){
                    return "ip : "+sshdto.getIp()+"已存在！";
                }
            }
        }
        list.add(sshdto);
        FileUtils.createFile(DEPLOY_PATH,SSH,gson.toJson(list).getBytes());
        return null;
    }
}
