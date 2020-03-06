package com.shiro.pojo.re;

import lombok.Data;

import java.io.Serializable;

/**
 * DATE: 2019/9/5 16:26
 * USER: create by shen
 */
@Data
public class ReturnMessage {
    Integer code = 200;
    Object data;
    String message;

    public void error(Exception e){
        code = 500;
        message = e.getClass().getSimpleName()+":"+ e.getMessage();
    }
}
