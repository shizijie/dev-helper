package com.shizijie.dev.helper.common;

/**
 * @author shizijie
 * @version 2019-11-11 下午5:47
 */

public class BaseController {
    private final static String SUCCESS_CODE="000000";
    private final static String SUCCESS_MSG="操作成功！";
    private final static String FAIL_CODE="999999";
    private final static String FAIL_MSG="操作失败，请稍后重试！";

    protected <T> ResponseBean<T> success(){
        return success(null);
    }

    protected <T> ResponseBean<T> success(T result){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setCode(SUCCESS_CODE);
        responseBean.setMsg(SUCCESS_MSG);
        responseBean.setResult(result);
        return responseBean;
    }

    protected <T> ResponseBean<T> fail(){
        return fail(FAIL_MSG);
    }

    protected <T> ResponseBean<T> fail(String msg){
        ResponseBean responseBean=new ResponseBean();
        responseBean.setCode(FAIL_CODE);
        responseBean.setMsg(msg==null?FAIL_MSG:msg);
        return responseBean;
    }

    protected <T> ResponseBean<T> back(String result){
        if(result!=null){
            return fail(result);
        }
        return success();
    }
}
