package com.shiro.web;

import com.shiro.pojo.re.ReturnMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class AllExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ReturnMessage handlerAllException(Exception e){
        ReturnMessage r = new ReturnMessage();
        AllExceptionHandler.log.error("Exception:{},cause:{}",e.getClass().getSimpleName(),e.getMessage());
        e.printStackTrace();
        r.error(e);
        return r;
    }

}