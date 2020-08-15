package com.shizijie.dev.helper.web.deploy.service;

import com.shizijie.dev.helper.web.deploy.dto.SSHDTO;

import java.util.List;

/**
 * @author shizijie
 * @version 2020-08-01 上午10:05
 */
public interface DeployService {
    String SSH="ssh.txt";

    List<SSHDTO> listSSH();

    String addService(SSHDTO sshdto);
}
