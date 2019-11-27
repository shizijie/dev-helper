package com.shizijie.dev.helper.web.advice;

import com.shizijie.dev.helper.web.common.BaseController;
import com.shizijie.dev.helper.web.common.ResponseBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author shizijie
 * @version 2019-11-11 下午5:35
 */
@RestControllerAdvice
public class ExceptionAdvice extends BaseController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBean bindException(MethodArgumentNotValidException exception){
        BindingResult bindingResult=exception.getBindingResult();
        String msg="";
        for(FieldError fieldError:bindingResult.getFieldErrors()){
            if(StringUtils.isBlank(msg)){
                msg+=fieldError.getDefaultMessage();
            }else{
                msg+=" , "+fieldError.getDefaultMessage();
            }
        }
        return fail(msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseBean parameterTypeException(HttpMessageNotReadableException exception){
        return fail("body is missing,json序列化错误！");
    }
}
